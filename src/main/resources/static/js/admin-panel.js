showUsers();

function showUsers() {
    $('#table-title').text('Users');
    $('#table-content').html('');
    getRequest('/api/users').then(data => {
        $('#table-head').html(`
         <th scope="col">Id</th>
         <th scope="col">Email</th>
         <th scope="col">Name</th>
         <th scope="col">Disabled</th>
    `)
        data.forEach((user) => {
            $('#table-content').append(`
            <tr>
                <th scope="row">${user.id}</th>
                <td>${user.email}</td>
                <th>${user.name}</th>
                <th><input type="checkbox" id="checkbox-${user.id}" onchange="disableUser(this, ${user.id})" class="form-check-input my-checkbox" style="margin: 0"></th>
            </tr>
        `);
            $(`#checkbox-${user.id}`).prop('checked', user.disabled);
        });
    });
}

function showGroups() {
    $('#table-title').text('Groups');
    $('#table-content').html('');
    getRequest('/api/groups').then(data => {
        $('#table-head').html(`
         <th scope="col">Id</th>
         <th scope="col">Name</th>
         <th scope="col">Owner id</th>
         <th scope="col">Delete</th>
        `)
        data.forEach((group) => {
            $('#table-content').append(`
            <tr>
                <th scope="row">${group.id}</th>
                <td>${group.name}</td>
                <th>${group.ownerId}</th>
                <th><button type="button" class="btn btn-danger" onclick="deleteGroup($(this).closest('th'), ${group.id})">Delete</button></th>
            </tr>
        `);
        });
    });
}

function showCourses() {
    $('#table-title').text('Courses');
    $('#table-content').html('');
    getRequest('/api/courses').then(data => {
        $('#table-head').html(`
         <th scope="col">Id</th>
         <th scope="col">Name</th>
         <th scope="col">Owner id</th>
         <th scope="col">Delete</th>
    `)
        data.forEach((course) => {
            $('#table-content').append(`
            <tr>
                <th scope="row">${course.id}</th>
                <td>${course.name}</td>
                <th>${course.ownerId}</th>
                <th><button type="button" class="btn btn-danger" onclick="deleteCourse($(this).closest('th'), ${course.id})">Delete</button></th>
            </tr>
        `);
        });
    });
}

function disableUser(checkbox, id) {
    let confirmed = confirm('Do you want to disable this user?');
    let isChecked = $(checkbox).is(":checked");
    if (confirmed) {
        deleteRequest(`/api/users/${id}/disabled?disabled=${isChecked}`);
    } else {
        $(checkbox).prop('checked', !isChecked);
    }
}

function deleteGroup(htmlElement, id) {
    let confirmed = confirm('Do you want to delete this group?');

    if (confirmed) {
        deleteRequest(`/api/groups/${id}`).then((res) => {
            $(htmlElement).parent().remove()
        });
    }
}

function deleteCourse(htmlElement, id) {
    let confirmed = confirm('Do you want to delete this course?');
    if (confirmed) {
        deleteRequest(`/api/courses/${id}`).then((res) => {
            $(htmlElement).parent().remove()
        });;
    }
}

function toggleButtons(button) {
    $('.my-btn').each((i, el) => {
        $(el).removeClass('active');
    })
    $(button).addClass('active');
}

function getRequest(url) {
    return $.get(url);
}

function deleteRequest(url) {
    return $.ajax({
        url: url,
        type: 'DELETE'
    });
}
