const urlParams = new URLSearchParams(window.location.search);
const groupId = urlParams.get('id');

let report;
let group;

init();

function init() {

    let noGroupText = '<div style="height: 70vh; display: flex; justify-content: center"><span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">Oops... cannot find such group</span></div>';

    if (groupId === null || groupId === undefined) {
        $('.wrapper').html(noGroupText);
    }

    $.ajax(`/api/v1/groups/${groupId}`).then(resGroup => {
        group = resGroup;

        $.ajax(`/api/v1/users/me`).then(user => {
            if (group.ownerId === user.id) {
                fillPage();
            } else {
                $('.wrapper').html(noGroupText);
            }
        })

    }).catch(err => $('.wrapper').html(noGroupText));
}
function fillPage() {
    const linkBack = $('#back-to-group');
    $(linkBack).html(`${group.name}`);
    $(linkBack).attr('href', `/group?id=${groupId}`);

    $.ajax(`/api/v1/statistic/${groupId}`).then(groupReport => {
        report = groupReport;
        generateGraphStatistic();
        generateStatisticTable();
    });
}

function generateStatisticTable() {

    const tableContent = $('#table-content');
    const assignments = group.assignments;

    assignments.forEach(assignment => {
        $('#table-head').append(`<th scope="col">${assignment.name}</th>`);
    })

    $(tableContent).append(`<tr>`)
    group.users.forEach(user => {
        $(tableContent).append(`<tr>`)
        $(tableContent).append(`<td>${user.name}</td>`)

        assignments.forEach(assignment => {
            let userReport = report.find(obj => obj.userId === user.id);
            const foundAssignment = userReport.assignments.find(userAssignment => assignment.id === userAssignment.assignmentId);
            let grade = foundAssignment === undefined ? '—' : foundAssignment.grade;
            $(tableContent).append(`<td>${grade}</td>`)
        })

        $(tableContent).append(`</tr>`)
    })

}

function generateGraphStatistic() {

    let dataArray = [];
    let labelsArray = [];

    group.assignments.forEach(assignment => {
        dataArray.push(countUsersThatFinishedAssignment(assignment));
        labelsArray.push(assignment.name)
    })


    let ctx = $('#myChart');
    let data = {
        labels: labelsArray,
        datasets: [{
            data: dataArray,
            borderColor: 'rgba(54, 162, 235, 1)',
            pointBorderColor: 'rgba(255, 99, 132, 1)',
            label: 'Count of answers for tasks'
        }],
    };
    let myLineChart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: Chart.defaults.line
    });
}

function countUsersThatFinishedAssignment(assignment) {
    let counter = 0;
    report.forEach(report => {
        const foundAssignment = report.assignments.find(userAssignment => assignment.id === userAssignment.assignmentId);
        if (foundAssignment !== undefined) counter++;
    })
    return counter;
}
