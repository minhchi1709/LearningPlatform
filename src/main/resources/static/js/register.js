const button = document.getElementById("register-user-button");
button.addEventListener('click', (e) => {
    const firstname = document.getElementById('firstname');
    const lastname = document.getElementById('lastname');
    const username = document.getElementById('username');
    const email = document.getElementById('email');
    const password = document.getElementById('password');

    fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        body: JSON.stringify({
            firstname: firstname.value,
            lastname: lastname.value,
            username: username.value,
            email: email.value,
            password: password.value
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    })
        .then(response => response.json())
        .then(json => {
            alert(json)
        });
window.location.replace("http://localhost:8080/auth/register")
});