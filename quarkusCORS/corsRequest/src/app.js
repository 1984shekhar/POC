async function loadPage(){
    const response = await fetch("http://localhost:8080/hello-resteasy");
    let message = await response.text();
    console.log(`${message} from quarkus resteasy application`);
    document.body.innerHTML = message;
}

loadPage();