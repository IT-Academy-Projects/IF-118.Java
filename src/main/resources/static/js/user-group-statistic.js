const urlParams = new URLSearchParams(window.location.search);
const groupId = urlParams.get('id');

let assignments;
let userReport;

init();

function init() {

    let noGroupText = '<div style="height: 70vh; display: flex; justify-content: center"><span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">Oops... cannot find such user or group</span></div>';
    if (groupId === null || groupId === undefined) {
        $('.wrapper').html(noGroupText);
    }

    $.ajax(`/api/v1/groups/${groupId}`).then(group => {
        assignments = group.assignments;
        $.ajax(`/api/v1/users/me`).then(user => {
            if (user.groups.find(userGroup => userGroup.id === group.id) !== undefined) {
                fillPage(group.name, user.id);
            } else {
                $('.wrapper').html(noGroupText);
            }
        })

    }).catch(err => $('.wrapper').html(noGroupText));
}

function fillPage(groupName, userId) {
    const linkBack = $('#back-to-group');
    $(linkBack).html(`${groupName}`);
    $(linkBack).attr('href', `/group?id=${groupId}`);

    $.ajax(`/api/v1/statistic/${groupId}/${userId}`).then(report => {
        userReport = report;
        generateDonutsStatistic();
        generateStatisticTable();
    });
}

function generateStatisticTable() {
    assignments.forEach(assignment => {
        const foundAssignment = userReport.assignments.find(userAssignment => assignment.id === userAssignment.assignmentId);
        let grade = foundAssignment === undefined ? '—' : foundAssignment.grade;

        $('#table-content').append(`
            <tr>
                <td>${assignment.name}</td>
                <th>${grade}</th>
            </tr>
        `);
    });
    $('#table-content').append(`
            <tr>
                <td></td>
                <th>Average — ${userReport.avg}</th>
            </tr>
    `);
}

function generateDonutsStatistic() {

    let finishedWorksCount = userReport.assignments.length;
    let unfinishedWorksCount = assignments.length - finishedWorksCount;

    if (finishedWorksCount === undefined || finishedWorksCount === null) {
        $('.diagram-wrapper').hide();
        return;
    }

    let ctx = $('#myChart');
    let data = {
        datasets: [{
            data: [unfinishedWorksCount, finishedWorksCount],
            backgroundColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)'
            ],
            borderColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)'
            ],
            borderWidth: 1
        }],
        labels: [
            'Red - Not finished works',
            'Blue - Finished works'
        ]
    };
    let myDoughnutChart = new Chart(ctx, {
        type: 'doughnut',
        data: data,
        options: Chart.defaults.doughnut
    });
    $('#diagram-info').html(`Amount of all tasks — ${assignments.length}`)
}