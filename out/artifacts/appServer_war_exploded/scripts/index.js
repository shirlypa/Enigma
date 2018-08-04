let playerType = null;
const UBOAT = "UBOAT";
const ALIES = "ALIES";

$(document).ready(() => {
    $("#uboat").click(() => {
        playerType = UBOAT
    });

    $("#alies").click(() => {
        playerType = ALIES
    });

    $("#login").click(() => {
        $('#errorMsg').hide();
        const userName = $("#userName").val();
        if (!userName){
            return showErr("Please enter a user name");
        }
        if (!playerType){
            return showErr("Please select uboat or alies")
        }
        $.get(`/login?userName=${userName}&userType=${playerType}`,(data, status) => {
            if (status === "success" && data > 0){
                if (playerType === UBOAT){
                    location.href = `/Uboat/createRoom.html?userName=${userName}`;
                } else {
                    const nextPage = "abc";
                    location.href = `/${nextPage}?port=${data}`;
                }
            } else {
                showErr(`The name ${userName} already used. Please select another name`)
            }
        });
    });
});

const showErr = (errorMsg) => {
    $('#errorMsg').text(errorMsg).show();
};