fetch("http://localhost:8080/token").then(() => {
    //document.cookie = 'token=AlaMaKota'
    connectToSSE();
});

function connectToSSE() {
    const evtSource = new EventSource("http://localhost:8080/stream-sse", {
        withCredentials: true,
    });

    evtSource.addEventListener('msg', data => {
        const newElement = document.createElement("li");
        const eventList = document.getElementById("list");
        newElement.textContent = `message: ${data.data}`;
        eventList.appendChild(newElement);
    });
}