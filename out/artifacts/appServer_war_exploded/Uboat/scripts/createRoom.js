$(document).ready(()=>{
    $("#uploadXmlForm").submit(() => {
        const params = new FormData($("#uploadXmlForm")[0]);
        console.log(params)
        //fd.append("CustomField", "This is some extra data");
        $.ajax({
            url: '/uploadXml',
            type: 'POST',
            data: params,
            success: data => {
                if (!data.success) return showError(data.payload);
                alert('success!');
            },
            error: function(err) {
                console.log("Failed to load the file");
            },
            contentType: false,
            processData: false
        });

        return false;
    });

    $('#fileInput').change(() => {
        $('#validationErrorContainer').hide();
        $('#errorList').empty();
    })
});

const showError = errors => {
    errors.forEach(err => $('#errorList').append(`<li>${err}</li>`));
    $('#validationErrorContainer').show();
}