console.log('Login works!')

//Hardcoded user until authorization is ready
$.get('api/users/1').then(data => {
    localStorage.setItem("userId", data.id)
})