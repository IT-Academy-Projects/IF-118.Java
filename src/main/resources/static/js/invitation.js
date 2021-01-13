function onSubmit() {
  let body = {
    email: document.getElementById("email").value,
    courseId: parseInt(document.getElementById("courseId").value, 10),
    groupId: parseInt(document.getElementById("groupId").value, 10),
  };
  fetch("http://localhost:8080/api/v1/invitation", {
    method: "POST",
    body: JSON.stringify(body),
    headers: {
      "Content-Type": "application/json",
    },
  });
}
