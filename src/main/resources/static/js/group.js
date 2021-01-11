showGroupInfo();
function showGroupInfo() {
    $('#table-content').html('');
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    $.ajax(`/api/v1/groups/${id}`).then(group => {
        $('#table-head').html(`
         <th scope="col">Id</th>
         <th scope="col">Name</th>
         <th scope="col">Owner id</th>
    `)
        $('#table-content').append(`
            <tr>
                <td>${group.id}</td>
                <td>${group.name}</td>
                <td>${group.ownerId}</td>
            </tr>
        `)
    });
}