let noCourseText = '<span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">Oops... Cannot find such course.</span>';
let noMaterialsText = '<span style="color: grey; align-self: center; margin-top: 10px">There is no materials yet.</span>';

initPage();

function initPage() {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    if (id !== undefined && id !== null && id !== '') {
        getCourse(id);
    } else {
        $('#course-content').html(noCourseText)
    }
}

function getCourse(id) {
    getRequest(`/api/v1/courses/${id}`).then(course => {

        $('#course-name').text(course.name);
        getRequest(`/api/v1/users/${course.ownerId}`).then(user => {
            $('#teacher-name').text(user.name);
        })

        if (course.materialIds.length > 0) {
            getMaterials(course.materialIds);
        } else {
            $('#materials').html(noMaterialsText);
        }
    }, err => {
        $('#course-content').html(noCourseText);
    });
}

function getMaterials(materialIds) {
    materialIds.forEach(materialId => {
        getRequest(`/api/v1/materials/${materialId}`).then(material => {
            $('#materials').append(`
                <div class="material">
                    <div class="material-name">${material.name}</div>
                    <div class="material-description">${material.description}</div>
                    <div class="material-download">Download: <a href="/api/v1/materials/${material.id}/file">${material.name}</a></div>
                    <div><a onclick="toggleComments(${materialId});">Comments</a></div>
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
                </div>
            `);
        });
        loadComments(materialId);
    });
}

function getRequest(url) {
    return $.get(url);
}
