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
                    <select onchange="selectRotor(${i},this.value)" class="w3-select w3-border w3-margin">
                        ${renderRotorIds(availableRotorCouns)}
                    </select>
                    <select onChange="selectRotorPosition(${i},this.value)" class="w3-select w3-border w3-margin">
                        ${renderAlphabetOptions(machineDescriptor.Alphabet)}
                    </select>
                </div>
            `)
        }
        $('#reflectorContainer').append(`
            <select onchange="selectReflector(this.value)" class="w3-select w3-border w3-margin" style="width: 80%">
                        ${renderReflectors(Object.values(machineDescriptor.AvaliableReflector))}
            </select>
        `)
    })
    $('#btnProcessStr').click(validateAndProcessStr)

    $('#btnRandom').click(() => randomSecret = true)

    pullUpdate();
    setInterval(pullUpdate,5000);
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
    $('#secretFormContainer').hide();
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
            $('#candidateStrings').fadeIn();
            $('#BeforeAfterEncodeingContainer').fadeIn();
            pullUpdate();
        },
        error: err => alert(JSON.stringify(err)),
    })
}

const pullUpdate = () => {
    $.get('/UboatUpdate',data => {
        $('#json').text(JSON.stringify(data));
        $('.aliesesContainer').empty();
        $('#successStringContainer').empty();
        data.aliesList.forEach(alies => {
            $('.aliesesContainer').append(renderAlly(alies));
        })
        Object.keys(data.successedStrings).forEach(aliesName => {
                $('#successStringContainer').append(renderAgents(aliesName,data.successedStrings[aliesName]))
            }
        )
        $('#battleName').text(data.roomName);
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
const renderAgents = (aliesName, strings) => {
    const res = (
    `
    <div class="aliesStrings">
        <b>${aliesName}</b><br/>   
        ${strings.reduce((string,acc) => acc + "<br/>" + string,'')}
    </div>
    `
)
    console.log(res);
    return res;
}

const renderAlly = ({name, agentNumber, ready}) => `
    <div class="w3-card-4 w3-round w3-margin" style="min-width: 150px">
            <header class="w3-container w3-card-2 w3-center w3-light-blue w3-padding">${name}</header>
            <div class="w3-center w3-padding">Agents: ${agentNumber}</div>
            <footer class="w3-container w3-center w3-light-${ready ? 'green' : 'grey'} w3-border-top w3-padding"><b>Ready</b></footer>
        </div>
`
