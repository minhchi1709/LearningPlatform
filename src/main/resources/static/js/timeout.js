const seconds = document.getElementById("seconds");
const period = 1000;
const time = 5000;
seconds.innerText = time / 1000;
let now = time;
setTimeout(() => {
    window.location.replace("http://localhost:8080/auth/login");
}, time)
setInterval(() => {
    now -= period;
    seconds.innerText = now/1000;
}, period)