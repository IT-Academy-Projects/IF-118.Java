let materialId;
let noMaterialText = '<span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">Oops... Cannot find such course.</span>';

initPage();



function initPage() {
    const urlParams = new URLSearchParams(window.location.search);
    materialId = urlParams.get('id');
    if (materialId !== undefined && materialId !== null && materialId !== '') {
        getMaterial(materialId);
    } else {
        $('#main').html(noMaterialText)
    }
}

function getMaterial(id) {
    getRequest(`/api/v1/materials/${id}`).then(material => {
        $('#materials').append(`
                <div class="material">
                    <div class="material-name">${material.name}</div>
                    <div class="material-description">${material.description}</div>
                    <div class="material-download">Download: <a href="/api/v1/materials/${material.id}/file">${material.name}</a></div>
                </div>
            `);
        material.assignments.forEach(assignment => {
            $('#assignments').append(`
                <div class="assignment">
                    <div class="assignment-name">${assignment.name}</div>
                    <div class="assignment-description">${assignment.description}</div>
                </div>
            `);
        })
    })
}

function getRequest(url) {
    return $.get(url);
}
