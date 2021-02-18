let noCourseText = '<span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">Oops... Cannot find such course.</span>';
let noMaterialsText = '<span style="color: grey; align-self: center; margin-top: 10px">There is no materials yet.</span>';

let courseId;
let currentUserId;
let canEdit;

initPage();

function initPage() {
    generateFbShareButton();
    getRequest(`/api/v1/users/me`).then(user => {
        const urlParams = new URLSearchParams(window.location.search);
        currentUserId = user.id;
        courseId = urlParams.get('id');
        if (courseId !== undefined && courseId !== null && courseId !== '') {
            getCourse(courseId, user);
        } else {
            $('#course-content').html(noCourseText)
        }
    });
}

function getCourse(id, user) {
    getRequest(`/api/v1/courses/${id}`).then(course => {

        let role = user.roles.find(role => role.name === "TEACHER");

        if (role !== null && role !== undefined && currentUserId === course.ownerId) {
            canEdit = true;
        } else {
            canEdit = false;
            $('#add-material-btn').hide();
            $('#edit-course-description-block').hide();
            $('.invitation').hide();
        }

        $('#course-name').text(course.name);
        $('#description-value').text(course.description);
        getRequest(`/api/v1/users/${course.ownerId}`).then(user => {
            $('#teacher-name').text(user.name);
        })
        if (course.materialIds.length > 0) {
            if (canEdit) {
                getMaterialsForTeacher(course.id);
            } else {
                getMaterialsForStudent(course.id);
            }
        } else {
            $('#materials').html(noMaterialsText);
        }
    }, err => {
        $('#course-content').html(noCourseText);
    });
}

function getMaterialsForTeacher(courseId) {
    getRequest(`/api/v1/materials/in-course/${courseId}`).then(materials => {
        materials.forEach(material => {
            let deleteBtn = '';
                deleteBtn = `
                    <div class="delete-material"">
                        <button type="button" class="btn btn-danger" onclick="deleteMaterial(${material.id})">Delete</button>
                    </div>`;
            $('#materials').append(`
            <div class="material-info">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-4">
                            <div class="material-name"><a href="/material?id=${material.id}">${material.name}</a></div>
                            <div class="material-description">${material.description}</div>
                            <div class="material-download">Download: <a href="/api/v1/materials/${material.id}/file">${material.name}</a></div>
                            ${deleteBtn}
                        </div>
                        <div class="col-lg-4">
                            <div>
                                <button class="btn btn-outline-info btn-sm" onclick="toggleComments(${material.id});">View comments</button>
                                <button id="create-comment-button" type="button" class="btn btn-outline-success btn-sm"
                                    data-toggle="modal" data-target="#create-comment-modal" onclick="setMaterialId(${material.id})">Write comment
                                </button> 
                            </div>
                        </div>
                        <div class="col-lg-4">
                            <div id="material-${material.id}-comments" style="display: none">
                                <div id="material-${material.id}-comments-body">
                                </div>
                            </div>
                        </div>
                    </div>
                    
                </div>
            </div>
        `);
        loadComments(material.id);
        });
    });
}

function getMaterialsForStudent() {

}

function createMaterial() {
    let data = {
        name: $('#name').val(),
        description: $('#description').val(),
        courseId: courseId,
        ownerId: currentUserId
    }

    let file = $('#file').prop('files')[0];

    let formData = new FormData();
    formData.append('file', file);
    formData.append('material', JSON.stringify(data));
    postRequest(`/api/v1/materials`, formData).then(res => {
        $('#materials').html('');
        getRequest(`/api/v1/courses/${courseId}`).then(course => {
            getMaterialsForTeacher(course.id);
            $('#create-material-modal').modal('hide');
        })
    });
}

function editCourseDescription() {
    let newDescription = $('#description-textarea').val();
    let data = {description: newDescription};
    patchRequest(`/api/v1/courses/${courseId}/description`, data).then(res => {
        $('#description-value').text(newDescription);
    })
}

function checkTextArea() {
    if ($('#description-textarea').val().length > 0) {
        $('#submit-edit-desc-btn').removeAttr('disabled');
    } else {
        $('#submit-edit-desc-btn').attr('disabled', true);
    }
}

function checkInputsValue() {
    if ($('#name').val().length > 0 && $('#description').val().length > 0 && $('#file').val().length > 0) {
        $('#submit-create-material-btn').removeAttr('disabled');
    } else {
        $('#submit-create-material-btn').attr('disabled', true);
    }
}

function deleteMaterial(id) {
    deleteRequest(`/api/v1/materials/${id}`).then(res => {
        $('#materials').html('');
        getRequest(`/api/v1/courses/${courseId}`).then(course => {
            getMaterialsForTeacher(courseId);
        })
    })
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

function postRequest(url, data) {
    return $.ajax({
        url: url,
        type: 'POST',
        data: data,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data'
    });
}

function patchRequest(url, data) {
    return $.ajax({
        url: url,
        type: 'PATCH',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    })
}

function generateFbShareButton() {
    $("#fb-share-button").append(`
    <div class="fb-share-button" data-href="https://softclass.herokuapp.com/course?id=${courseId}"
         data-layout="button_count"
         data-size="small">
        <a target="_blank" href="https://www.facebook.com/sharer/sharer.php?u=https%3A%2F%2Fsoftclass.herokuapp.com%course%3Fid%3D${courseId}&amp;src=sdkpreparse"
           class="fb-xfbml-parse-ignore">
            Share
        </a>
    </div>
   `);
    console.log("I'm here")
}