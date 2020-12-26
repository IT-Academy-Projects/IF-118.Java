let user = JSON.parse(localStorage.getItem("user"))

$('#profile-btn').text(user.name)