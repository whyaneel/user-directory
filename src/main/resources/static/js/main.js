var GET_USERS = 'api/users';
var GET_USER_BY_ID = 'api/users/{id}';
var DELETE_USER_BY_ID = 'api/users/{id}';
$(document).ready(function() {
    fetchAllUsers();
});

function displayUsers(data) {
    var resultsContent = '';
    $.each(data, function(k, v) {
        resultsContent +=
            '<tr>' +
            '<td>' + v.firstName + ' ' + v.lastName + '</td>' +
            '<td>' + (v.addresses.length > 0 ? v.addresses[0] : '') + '</td>' +
            '<td>' +
            '<button class="btn-softdelete" type="button" onclick="softDelete(\'' + v.id + '\')">-</button>' +
            '<button class="btn-delete" type="button" onclick="permDelete(\'' + v.id + '\')">x</button>' +
            '</td>' +
            '</tr>';
    });
    $(".results>table>tbody").html(resultsContent);
}

function fetchAllUsers() {
    $.get(GET_USERS, function(data, status) {
        displayUsers(data);
    });
}

function softDelete(id) {
    alert("TODO soft deleted " + id);
}

function permDelete(id) {
    $.ajax({
        url: DELETE_USER_BY_ID.replace('{id}', id),
        type: 'DELETE',
        success: function(result) {
            fetchAllUsers();
            alert("deleted " + id);
        }
    });
}

//TODO this should become `search` with multiple combinations
function searchUser() {
    var id = $('#search').val();
    if (id.length > 3) {
        $.ajax({
            url: GET_USER_BY_ID.replace('{id}', id),
            type: 'GET',
            success: function(result) {
                displayUsers([result]);
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
}