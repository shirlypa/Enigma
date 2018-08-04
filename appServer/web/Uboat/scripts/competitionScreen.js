let machineDescriptor;
const rotors = [];
const rotorsPosition = [];
let reflector = null;

$(document).ready(() => {
    $.get(`/MachineDescriptor?roomName=battlefield`,(data, status) => {
        machineDescriptor = data;
        console.log(data);
        const availableRotorCouns = Object.keys(data.AvaliableRotors).length;
        for (let i = 0; i < data.RotorsInUseCount; i++){
            $('#rotorsContainer').append(`
                <div style="display:flex">
                    <select onchange="selectRotor(${i},this.value)">
                        ${renderRotorIds(availableRotorCouns)}
                    </select>
                    <select onChange="selectRotorPosition(${i},this.value)">
                        ${renderAlphabetOptions(data.Alphabet)}
                    </select>
                </div>
            `)
        }
        $('#reflectorContainer').append(`
            <select onchange="selectReflector(this.value)">
                        ${renderReflectors(Object.values(data.AvaliableReflector))}
            </select>
        `)
    })
    $('#btnProcessStr').click(validateAndProcessStr)
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

function validation(validStr, txtToProcess) {
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
    if (!txtToProcess) {
        validStr += '<li>You must enter string to process</li>'
    }
    if (!isFromDictionary(txtToProcess)) {
        validStr += '<li>You must enter words from dictionary</li>'
    }
    return validStr;
}

const validateAndProcessStr = () => {
    const txtToProcess = $('#txtToProcess').val().trim().toLowerCase();

    let validStr = "";
    $('#validationError').hide();
    $('#errList').empty();

    validStr = validation(validStr, txtToProcess);

    if (validStr) return showValidationError(validStr);

    $.ajax({
        type: 'POST',
        url: '/ProcessString',
        data: {secret: createSecretJson(), str: txtToProcess},
        success: data => alert('sucess' + JSON.stringify(data)),
        error: err => alert(JSON.stringify(err)),
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
            Position: rotorsPosition[i],
        })
    }
    return JSON.stringify({
        RotorsInUse,
        ReflectorId: reflector,
    });
}