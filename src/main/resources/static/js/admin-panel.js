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
                <th><input type="checkbox" id="user-checkbox-${user.id}" onchange="disableUser(this, ${user.id})" class="form-check-input my-checkbox" style="margin: 0"></th>
            </tr>
        `);
            $(`#user-checkbox-${user.id}`).prop('checked', user.disabled);
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
         <th scope="col">Disabled</th>
        `)
        data.forEach((group) => {
            $('#table-content').append(`
            <tr>
                <th scope="row">${group.id}</th>
                <td>${group.name}</td>
                <th>${group.ownerId}</th>
                <th><input type="checkbox" id="group-checkbox-${group.id}" onchange="disableGroup(this, ${group.id})" class="form-check-input my-checkbox" style="margin: 0"></th>
            </tr>
        `);
            $(`#group-checkbox-${group.id}`).prop('checked', group.disabled);
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
         <th scope="col">Disabled</th>
    `)
        data.forEach((course) => {
            $('#table-content').append(`
            <tr>
                <th scope="row">${course.id}</th>
                <td>${course.name}</td>
                <th>${course.ownerId}</th>
                <th><input type="checkbox" id="courses-checkbox-${course.id}" onchange="disableCourse(this, ${course.id})" class="form-check-input my-checkbox" style="margin: 0"></th>
            </tr>
        `);
            $(`#courses-checkbox-${course.id}`).prop('checked', course.disabled);
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

function disableGroup(checkbox, id) {
    let confirmed = confirm('Do you want to delete this group?');
    let isChecked = $(checkbox).is(":checked");
    if (confirmed) {
        deleteRequest(`/api/groups/${id}/disabled?disabled=${isChecked}`);
    } else {
        $(checkbox).prop('checked', !isChecked);
    }
}

function disableCourse(checkbox, id) {
    let confirmed = confirm('Do you want to delete this group?');
    let isChecked = $(checkbox).is(":checked");
    if (confirmed) {
        deleteRequest(`/api/courses/${id}/disabled?disabled=${isChecked}`);
    } else {
        $(checkbox).prop('checked', !isChecked);
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
