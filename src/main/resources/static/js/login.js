console.log('Login works!')

//Hardcoded user until authorization is ready
$.get('api/users/1').then(data => {
    localStorage.setItem("user", JSON.stringify(data))
    console.log(JSON.parse(localStorage.user))
})