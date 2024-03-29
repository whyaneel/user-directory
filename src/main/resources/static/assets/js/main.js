var GET_USERS = 'api/users';
var GET_USER_BY_ID = 'api/users/{id}';
var DELETE_USER_BY_ID = 'api/users/{id}';
var GET_USERS_INACTIVE = 'api/inactive/users';
var GET_USER_BY_ID_INACTIVE = 'api/inactive/users/{id}';
var DELETE_USER_BY_ID_INACTIVE = 'api/inactive/users/{id}';
active = true;
ID_TOKEN = "";
var INDEX_PAGE = "/index.html"
var SEARCH_PAGE = "/search.html"

$(document).ready(function() {
    firebase.auth().onAuthStateChanged(function(currentUser) {
        if (currentUser) {
            // User is signed in.
            ID_TOKEN = currentUser.pa
            if (!isSearchPage()) window.location = SEARCH_PAGE
        } else {
            ID_TOKEN = ""
            // NO User is signed in.
            if (!isIndexPage()) window.location = INDEX_PAGE
        }
    });

    setTimeout(function() {
        if (isSearchPage()) fetchAllUsers();
    }, 1000);
});

function isSearchPage() {
    return (window.location.pathname == SEARCH_PAGE)
}

function isIndexPage() {
    return (window.location.pathname == INDEX_PAGE)
}

function displayUsers(data) {
    var resultsContent = '';
    $.each(data, function(k, v) {
        resultsContent +=
            '<tr>' +
            '<td>' + v.id + '</td>' +
            '<td>' + v.firstName + ' ' + v.lastName + '</td>' +
            '<td>' + (v.addresses.length > 0 ? v.addresses[0] : '') + '</td>' +
            '<td>';
        if (active)
            resultsContent += '<button class="btn-softdelete" type="button" onclick="deleteRecord(\'' + v.id + '\')">-</button>';
        else
            resultsContent += '<button class="btn-delete" type="button" onclick="deleteRecord(\'' + v.id + '\')">x</button>';

        resultsContent += '</td>' +
            '</tr>';
    });
    $(".results>table>tbody").html(resultsContent);
}

function fetchAllUsers() {
    var byDefaultFetchUrl = (active ? GET_USERS : GET_USERS_INACTIVE)
    fetchUsers(byDefaultFetchUrl)
}

function deleteRecord(id) {
    $.ajax({
        url: (active ? DELETE_USER_BY_ID : DELETE_USER_BY_ID_INACTIVE).replace('{id}', id),
        type: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + ID_TOKEN
        },
        success: function(result) {
            $('#search').val('');
            fetchAllUsers();
            alert("record (" + id + ") " + (active ? "soft deleted" : "deleted permanently"));
        }
    });
}

//TODO this should become `search` with multiple combinations
function searchUser() {
    var WINDOW_LOCATION_SEARCH = "?search={search}"
    var term = $('#search').val().trim();
    if (term.length > 3) {
        var bySearchTermUrl = (active ? GET_USERS : GET_USERS_INACTIVE) + WINDOW_LOCATION_SEARCH.replace('{search}', term)
        fetchUsers(bySearchTermUrl)
    }
}

function fetchUsers(url) {
    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Authorization': 'Bearer ' + ID_TOKEN
        },
        success: function(result) {
            displayUsers(result);
        },
        error: function(jqXHR, exception) {
            if (jqXHR.status === 0) {
                alert('Not connect.\n Verify Network.');
            } else if (jqXHR.status >= 400 && jqXHR.status < 499) {
                $.each(jqXHR.responseJSON.errors, function(index, error) {
                    alert(error.message)
                });
            } else if (jqXHR.status == 500) {
                alert('Internal Server Error [500].');
            }
        }
    });
}

function toggleToolbarStatus() {
    active = !active;
    $('#search').val('');
    fetchAllUsers();
}

function login() {
    var userEmail = document.getElementById("email_field").value;
    var userPass = document.getElementById("password_field").value;
    //TODO Password has to be encoded instead of plain text
    firebase.auth().signInWithEmailAndPassword(userEmail, userPass).catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        alert("Error : " + errorMessage);
    });
}

//TODO use on search page, so that user can be logged out
function logout() {
    firebase.auth().signOut().then(function() {
        // Sign-out successful.
    }).catch(function(error) {
        // An error happened.
    });
}