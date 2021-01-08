showCourses();
showCourseCreateForm();
function showCourses() {
    $('#table-content').html('');
    $.ajax(`/api/v1/courses`).then(data => {
        $('#table-head').html(`
         <th scope="col">Id</th>
         <th scope="col">Name</th>
    `)
        data.forEach((course) => {
            $('#table-content').append(`
            <tr>
                <td>${course.id}</td>
                <td><a href="">${course.name}</a></td>
            </tr>
        `);
        });
    });
}

function createCourse() {
    const courseRequest = {name: $('#name').val()};
    $.ajax({
        type: "POST",
        url: `/api/v1/courses`,
        data: JSON.stringify(courseRequest),
        contentType: "application/json; charset=utf-8",
        success: function() {
            showCourses();
        }
    });
}

function showCourseCreateForm() {
    $.ajax(`/api/v1/users/current`).then(data => {
        if (data){
            $('#create-course').show();
        }
    });
}