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
                alert('done');
                pullUpdate()
                setInterval(pullUpdate,5000);
            },
            error: err => console.log(JSON.stringify(err)),
        })
    })
})

const showValidationError = (errMsg) => {
    $('#validationError').show();
    $('#errList').append(errMsg);
}

const pullUpdate = () => {
    $.get('/AliesUpdate',data => {
        console.log(data);
        $('#update').text(JSON.stringify(data));
    })
}