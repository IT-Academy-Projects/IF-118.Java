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
