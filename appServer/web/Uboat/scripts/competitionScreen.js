let machineDescriptor;
const rotors = [];
const rotorsPosition = [];
let reflector = null;
let randomSecret = false;

$(document).ready(() => {
    $.get(`/MachineDescriptor`,(data, status) => {
        machineDescriptor = data;
        const availableRotorCouns = Object.keys(machineDescriptor.AvaliableRotors).length;
        for (let i = 0; i < machineDescriptor.RotorsInUseCount; i++){
            $('#rotorsContainer').append(`
                <div style="display:flex">
                    <select onchange="selectRotor(${i},this.value)">
                        ${renderRotorIds(availableRotorCouns)}
                    </select>
                    <select onChange="selectRotorPosition(${i},this.value)">
                        ${renderAlphabetOptions(machineDescriptor.Alphabet)}
                    </select>
                </div>
            `)
        }
        $('#reflectorContainer').append(`
            <select onchange="selectReflector(this.value)">
                        ${renderReflectors(Object.values(machineDescriptor.AvaliableReflector))}
            </select>
        `)
    })
    $('#btnProcessStr').click(validateAndProcessStr)

    $('#btnRandom').click(() => randomSecret = true)
})

const selectRotor = (pos, value) => {
    rotors[pos] = value;
}

const selectRotorPosition = (pos ,value) => {
    rotorsPosition[pos] = value;
}

const selectReflector = (value) => {
    reflector = value;
}


const renderRotorIds = (len) => {
    let str = "";
    str += `<option selected disabled value=0>Rotor</option>`
    for (let i = 1; i <= len; i++){
        str += `<option value=${i}>${i}</option>`
    }
    return str;
}

const renderAlphabetOptions = (alphabet) => {
    let str = "";
    str += `<option selected disabled value=0>Position</option>`
    for(let i = 0; i < alphabet.length; i++){
        const letter = alphabet.charAt(i);
        str += `<option value=${letter}>${letter}</option>`
    }
    return str;
}

const renderReflectors = (reflectors) => {
    let str = "";
    const romian = ['I','II','III','IV','V'];
    str += `<option selected disabled value=0>Reflector</option>`
    for(let i = 1; i <= reflectors.length; i++){
        str += `<option value=${i}>${romian[i - 1]}</option>`
    }
    return str;
}

const showValidationError = (errMsg) => {
    $('#validationError').show();
    $('#errList').append(errMsg);
}

function validation(txtToProcess) {
    let validStr = ""
    if (!txtToProcess) {
        validStr += '<li>You must enter string to process</li>'
    }
    if (!isFromDictionary(txtToProcess)) {
        validStr += '<li>You must enter words from dictionary</li>'
    }
    if (randomSecret) return validStr;
    if (new Set(rotors).size !== rotors.length) {
        validStr += '<li>you cant choose a rotor twice</li>';
    }
    if (rotors.length !== machineDescriptor.RotorsInUseCount) {
        validStr += '<li>You must select all rotors</li>';
    }
    if (rotorsPosition.length !== machineDescriptor.RotorsInUseCount) {
        validStr += '<li>You must select all rotors positions</li>';
    }
    if (!reflector) {
        validStr += '<li>You must select reflector</li>'
    }
    return validStr;
}

const validateAndProcessStr = () => {
    const txtToProcess = $('#txtToProcess').val().trim().toLowerCase();

    $('#validationError').hide();
    $('#errList').empty();

    const validStr = validation(txtToProcess);

    if (validStr) return showValidationError(validStr);

    $('#beforeString').text(txtToProcess.toUpperCase());
    $('#secretFormContainer').fadeOut();
    const data = {str: txtToProcess};
    if (randomSecret){
        data.random = "RANDOM";
        data.secret = null;
    } else {
        data.random = "";
        data.secret = createSecretJson();
    }
    $.ajax({
        type: 'POST',
        url: '/ProcessString',
        data,
        success: data => {
            $('#afterString').text(data.payload)
            pullUpdate()
            setInterval(pullUpdate,5000);
        },
        error: err => alert(JSON.stringify(err)),
    })
}

const pullUpdate = () => {
    $.get('/UboatUpdate',data => {
        $('#aliesesContainer').text(JSON.stringify(data));
    })
}

const isFromDictionary = txt => {
    const words = txt.split(" ");
    const specialChars = machineDescriptor.MachineDecipher.Words.SpecialChar;
    const dictionary = machineDescriptor.MachineDecipher.Words.Words;
    let cleanDictionary;
    for (let i = 0; i < specialChars.length; i++){
        const c = specialChars.charAt(i);
        cleanDictionary = dictionary.replace(new RegExp(c == '?'? `\\${c}`: c,'g'),'');
    }
    for (let i = 0; i < words.length ; i++){
        if (cleanDictionary.indexOf(` ${words[i]} `) < 0){
            return false;
        }
    }
    return true;
}

const createSecretJson = () => {
    const RotorsInUse = [];
    for (let i = 0; i < machineDescriptor.RotorsInUseCount; i++){
        RotorsInUse.push({
            RotorId: rotors[i],
            Position: {
                PositionAsChar: rotorsPosition[i].toLowerCase(),
                PositionAsInt: 0,
                IsLetter: false,
            }
        })
    }
    return JSON.stringify({
        RotorsInUse,
        ReflectorId: reflector,
    });
}

const renderAlieses = (alieses) => {

}