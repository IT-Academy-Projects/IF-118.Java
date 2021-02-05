let noInvitationsText = '<span style="text-align: center;">No invitations yet.</span>';

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
    groupId: group_id,
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
      let invId = invitation.id;
      let courseOrGroup = invitation.courseOrGroup;
      let courseOrGroupTeacher;
      let approveBtn = '';
      let deleteBtn = '';

      approveBtn = `<div class="approve-invitation btn">
                            <button type="button" class="btn btn-outline-success show" onclick="approveInvitation(${invId})">Approve</button>
                          </div>`;
      deleteBtn = `<div class="delete-invitation btn">
                            <button type="button" class="btn btn-outline-danger" onclick="deleteInvitation(${invId})">Delete</button>
                         </div>`;

      if( courseOrGroup == 'course'){
        getRequest(`api/v1/courses/${invitation.courseOrGroupId}`).then(course => {
          let courseName = course.name;
          courseOrGroupTeacher = course.ownerId;

          getRequest(`/api/v1/users/${courseOrGroupTeacher}`).then(user => {
            courseOrGroupTeacher = user.name;
            showInvitation('Course', courseName, courseOrGroupTeacher, approveBtn, deleteBtn);
          });
        });
      } else{
        getRequest(`api/v1/groups/${invitation.courseOrGroupId}`).then(group => {
          let groupName = group.name;
          courseOrGroupTeacher = group.ownerId;

          getRequest(`/api/v1/users/${courseOrGroupTeacher}`).then(user => {
            courseOrGroupTeacher = user.name;
            showInvitation('Group', groupName, courseOrGroupTeacher, approveBtn, deleteBtn);
          });
        });
      }
    });
  });
}

function showInvitation(courseOrGroup, courseOrGroupName, teacher, approveBtn, deleteBtn){
  $('#invitations').append(`
        <div class="card text-white bg-dark mb-3" style=" margin-right: 15px;" >
            <div class="card-header">Invitation to</div>
            <div class="card-body">
                <h5 class="card-title">${courseOrGroup} ${courseOrGroupName}</h5>
                <p class="card-text">Teacher: ${teacher}</p>
                <div class="btn" style="display: flex; justify-content: center;">
                    ${approveBtn}
                    ${deleteBtn}
                </div>
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
