$(document).ready(() => {
    $.get('/AliesPort',port => {
        $('#port').text(port);
    })

    $.get("/AvailableRooms",data => {
        data.rooms.forEach(room => {
            $('#roomsContainer').append(`
                <div class="roomContainer">
                    <b>${room.roomName}</b><br/>
                    Uboat Name: ${room.uboatName}<br/>
                    Status: Waiting<br/>
                    Process Level: ${room.processLevel}<br/>
                    Allies: ${room.registeredAllies}/${room.requiredAllies}
                    <div>
                        <button onClick='enterRoom("${room.roomName}")'>Let's Go</button>
                    </div>
                </div>
            `);
        })

    })
})
// enterRoom(${room.roomName})
const enterRoom = (roomName) => {
    $.ajax({
        type: 'POST',
        url: '/EnterRoom',
        data: {roomName},
        success: res => {
            if (!res) return alert('Oops! The battle just got full.. Try another one :)');
            location.href = `/Alies/CompetitionScreen.html`;
        },
        error: err => alert(JSON.stringify(err)),
    })
}