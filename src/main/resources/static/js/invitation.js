let noInvitationsText = '<span style="color: grey; margin-left: 42%; margin-top: 10px">There is no invitations yet.</span>';
let invId;
let courseOrGroup;
let courseOrGroupName;
let teacher;

function inviteToCourse() {
  let course_id = parseInt(window.location.toString().split("id=")[1])
  let body = {
    email: document.getElementById("email").value,
    courseId: course_id
  };
  $.ajax({
    type: "POST",
    url: `/api/v1/invitation`,
    data: JSON.stringify(body),
    contentType: "application/json; charset=utf-8",
    success: function () {
      getCourse(course_id);
      $("#close").click();
    }
  });
}

function inviteToGroup() {
  let group_id = parseInt(window.location.toString().split("id=")[1])
  let body = {
    email: document.getElementById("email").value,
    groupId: group_id
  };
  $.ajax({
    type: "POST",
    url: `/api/v1/invitation`,
    data: JSON.stringify(body),
    contentType: "application/json; charset=utf-8",
    success: function () {
      showGroupInfo(group_id);
      $("#close").click();
    }
  });
}

function refreshInvitations() {
    getRequest(`/api/v1/invitation`).then(invitations => {
        if (invitations.length > 0) {
            getInvitations();
        } else {
            $('#invitations').html(noInvitationsText);
        }
    });
}

function getInvitations() {
    getRequest(`/api/v1/invitation`).then(invitations => {
        invitations.forEach(invitation => {
            invId = invitation.id;
            courseOrGroup = invitation.courseOrGroup;

            let approveBtn = '';
            let deleteBtn = '';

            approveBtn = `<div class="approve-invitation btn">
                            <button type="button" class="btn btn-outline-success show" onclick="approveInvitation(${invId})">Approve</button>
                          </div>`;
            deleteBtn = `<div class="delete-invitation btn">
                            <button type="button" class="btn btn-outline-dark" onclick="deleteInvitation(${invId})">Delete</button>
                         </div>`;

            if( courseOrGroup == 'course'){
                getRequest(`api/v1/courses/${invitation.courseOrGroupId}`).then(course => {
                    courseOrGroupName = course.name;
                    teacher = course.ownerId;

                    getRequest(`/api/v1/users/${teacher}`).then(user => {
                        teacher = user.name;
                        showInvitation(courseOrGroup, courseOrGroupName, teacher, approveBtn, deleteBtn);
                    });
                });
            } else{
                getRequest(`api/v1/groups/${invitation.courseOrGroupId}`).then(group => {
                    courseOrGroupName = group.name;
                    teacher = group.ownerId;

                    getRequest(`/api/v1/users/${teacher}`).then(user => {
                        teacher = user.name;
                        showInvitation(courseOrGroup, courseOrGroupName, teacher, approveBtn, deleteBtn);
                    });
                });
            }
        });
    });
 }

function showInvitation(courseOrGroup, courseOrGroupName, teacher, approveBtn, deleteBtn){
    $('#invitations').append(`
        <div class="invitation-info">
                <div class="invitation-name">You have an invitation to the ${courseOrGroup} ${courseOrGroupName}</div>
                <div class="invitation-teacher">Teacher: ${teacher}</div>
                <div class="btn">  
                    ${approveBtn}
                    ${deleteBtn}
                </div>
        </div>  
    `);
}

function deleteInvitation(id) {
   deleteRequest(`/api/v1/invitation/${id}`).then(res => {
     $('#invitations').html('');
       refreshInvitations();
   })
 }
 function approveInvitation(id) {
   patchRequest(`/api/v1/invitation/approve/${id}`).then(res => {
     $('#invitations').html('');
       refreshInvitations();
   })
 }

function getRequest(url) {
    return $.get(url);
}

function patchRequest(url, data) {
    return $.ajax({
        url: url,
        type: 'PATCH',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    })
}

function deleteRequest(url) {
    return $.ajax({
        url: url,
        type: 'DELETE'
    });
}