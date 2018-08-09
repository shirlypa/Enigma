$(document).ready(() => {
    $('#btnSend').click(() => {
        $('#validationError').hide();
        $('#errList').empty();
        const inputMissionSize = $('#txtMissionSize').val()
        if (!inputMissionSize){
            return showValidationError("You must set the mission size");
        }
        $('#missionSizeContainer').fadeOut();
        $.ajax({
            type: 'POST',
            url: '/SetMissionSize',
            data: {missionSize: inputMissionSize},
            success: () => {
                pullUpdate()
            },
            error: err => console.log(JSON.stringify(err)),
        })
    })
    pullUpdate();
    setInterval(pullUpdate,5000);
})

const showValidationError = (errMsg) => {
    $('#validationError').text(errMsg).show();
}

const pullUpdate = () => {
    $.get('/AliesUpdate',data => {
        console.log(data);
        //$('#update').text(JSON.stringify(data));
        renderAgents(data.agents);
        $('#battleName').text(data.roomName);
        $('#uboatName').text(data.uboatName);
        $('#roomStatus').html(renderStatus(data))
        $('.other-allies-container').empty();
        data.otherAlies.forEach(alies => {
            $('.other-allies-container').append(renderAnotherAlly(alies))
        })
    })
}

const renderAgents = (agents) => {
    $('#agentsInfo').empty();
    agents.forEach((agent, index) => {
        $('#agentsInfo').append(renderAgentInfo(agent.successStrings,index + 1));
    })
}

const renderAgentInfo = (succStrNumber, index) => (`
    <tr>
        <td>${index}</td>
        <td>${succStrNumber}</td>
    </tr>
`)

const renderStatus = (update) => {
    switch(update.gameState){
        case "BATTLEFIELD_LOADED":
            return `Waiting for ${update.uboatName} encode a string`
        case "GOT_STRING_TO_PROCESS":
            return `Waiting for more allies to connect<br/>
                    your agents should to decode the string: ${update.strToProccess}`
        case "RUNNING":
            return `Run<br/>
                    your agents try to decode the string: ${update.strToProccess}`
        case "GAME_OVER":
            handleGameOver()
            return `Game Over<br/>
                    The Allies: ${update.winners} Won!`
    }
}

const renderAnotherAlly = ({name, agentNumber, ready}) => `
    <div class="w3-card-4 w3-round w3-margin" style="min-width: 150px">
            <header class="w3-container w3-card-2 w3-center w3-light-blue w3-padding">${name}</header>
            <div class="w3-center w3-padding">Agents: ${agentNumber}</div>
            <footer class="w3-container w3-center w3-light-${ready ? 'green' : 'grey'} w3-border-top w3-padding"><b>Ready</b></footer>
        </div>
`

const handleGameOver = () => {
    $('#exitModal').fadeIn();
    setTimeout(timerToExit,1000)
}
const timerToExit = () => {
    $('#timer').text(timerSeconds);
    timerSeconds--;
    if (timerSeconds === 0){
        location.href = `/Alies/PickRoom.html`
    } else {
        setTimeout(timerToExit,1000)
    }
}

let timerSeconds = 5;