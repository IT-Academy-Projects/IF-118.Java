let noCourseText = '<span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">Oops... Cannot find such course.</span>';
let noMaterialsText = '<span style="color: grey; align-self: center; margin-top: 10px">There is no materials yet.</span>';

let courseId;
let currentUserId;
let canEdit;

initPage();

function initPage() {
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
        }

        $('#course-name').text(course.name);
        $('#description-value').text(course.description);
        getRequest(`/api/v1/users/${course.ownerId}`).then(user => {
            $('#teacher-name').text(user.name);
        })

        refreshMaterials(course.materialIds);
    }, err => {
        $('#course-content').html(noCourseText);
    });
}

function refreshMaterials(ids) {
    if (ids.length > 0) {
        getMaterials(ids, canEdit);
    } else {
        $('#materials').html(noMaterialsText);
    }
}

function getMaterials(materialIds) {
    materialIds.forEach(materialId => {
        getRequest(`/api/v1/materials/${materialId}`).then(material => {
            let deleteBtn = '';
            if (canEdit) {
                deleteBtn = `<div class="delete-material"">
                                <button type="button" class="btn btn-danger" onclick="deleteMaterial(${materialId})">Delete</button>
                            </div>`;
            }
            $('#materials').append(`
                <div class="material-info">
                    <div>
                        <div class="material-name"><a href="/material?id=${materialId}">${material.name}</a></div>
                        <div class="material-description">${material.description}</div>
                        <div class="material-download">Download: <a href="/api/v1/materials/${material.id}/file">${material.name}</a></div>
                        <div>
                            <button class="btn btn-outline-info btn-sm" onclick="toggleComments(${materialId});">View comments</button>
                            <button id="create-comment-button" type="button" class="btn btn-outline-success btn-sm"
                                data-toggle="modal" data-target="#create-comment-modal" onclick="setMaterialId(${materialId})">Write comment
                            </button> 
                        </div>
                        <div id="material-${materialId}-comments" style="display: none">
                            <section>
                                <div class="container">
                                    <div class="row">
                                        <div class="col-sm-5 col-md-6 col-12 pb-4" id="material-${materialId}-comments-body">
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </div>
                        </br>
                    </div>
                    ${deleteBtn}
                </div>
            `);
        });
        loadComments(materialId);
    });
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
    formData.append( 'file', file);
    formData.append( 'material', JSON.stringify(data));
    postRequest(`/api/v1/materials`, formData).then(res => {
        $('#materials').html('');
        getRequest(`/api/v1/courses/${courseId}`).then(course => {
            refreshMaterials(course.materialIds);
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
    if ($('#description-textarea').val().length > 0 ) {
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
            refreshMaterials(course.materialIds);
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
