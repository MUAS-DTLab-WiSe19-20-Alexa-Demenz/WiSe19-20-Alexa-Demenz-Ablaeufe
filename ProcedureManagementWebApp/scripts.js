// Logs out of the app and clears all session variables and ids.
function logout() {
  amazon.Login.logout();
  window.location.replace('handle_logout.php');
}

// Calls delete script to remove the selected procedure from the database.
function deleteProcedure(btn) {
  var name = btn.parentNode.previousElementSibling.previousElementSibling.innerText;
  window.location.replace('./dynamodb_scripts/delete_procedure.php?name=' + name);
}

// Loads the procedure-editor page with a procedure name and origin (user or template) to load the contents of a procedure into the input fields.
// The name is retrieved depending on the clicked element (either <a> from the template-list, or button/<td> from the procedure list in dashboard.php).
function editProcedure(origin, btn) {
  var name = '';
  if (btn.nodeName === 'A') name = btn.innerText;
  else name = btn.parentNode.previousElementSibling.innerText;
  window.location.replace('procedure-editor.php?edit_name=' + name + '&edit_origin=' + origin);
}

// Shows the details of the procedure in the row where the arrow-button was pressed.
function expandRow(btn) {
  btn.attributes.getNamedItem('onclick').value = 'collapseRow(this)';
  btn.innerHTML = '&#9662;';
  btn.parentNode.parentNode.nextElementSibling.attributes.getNamedItem('style').value = '';
}
// Hides the details of the procedure in the row where the arrow-button was pressed.
function collapseRow(btn) {
  btn.attributes.getNamedItem('onclick').value = 'expandRow(this)';
  btn.innerHTML = '&#9666;';
  btn.parentNode.parentNode.nextElementSibling.attributes.getNamedItem('style').value = 'display:none;';
}

// shows more log entries in the table
function extendList(btn) { // btn is an a-tag in this case
  const ROWS_TO_EXPAND = 10;
  var addedRows = 0;
  var showMoreRow = btn.parentNode.parentNode;
  var currentRow = showMoreRow;

  while (currentRow.nextElementSibling !== null && addedRows < ROWS_TO_EXPAND) {
    currentRow.nextElementSibling.removeAttribute('style');
    currentRow = currentRow.nextElementSibling;
    addedRows++;
  }
  if (currentRow.nextElementSibling !== null) currentRow.insertAdjacentHTML('afterend', '<tr>' + showMoreRow.innerHTML + '</tr>');
  showMoreRow.remove();
}

// Help texts that are shown when the respective '?' button of a form input field is pressed.
const name_help = 'Dieser Name wird verwendet um den Ablauf zu starten\n(z.B. Ich will "Rausgehen")';
const repeat_help = 'Diese Einstellung legt fest an welchem Wochentag der Ablauf automatisch ausgelöst wird.';
const time_help = 'Die Uhrzeit bestimmt wann an dem ausgewählten Tag der Ablauf automatisch ausgelöst wird.';
const instr_txt_help = 'Der Anweisungstext wird gesagt wenn der Schritt beginnt.';
const help_txt_help = 'Der Hilfetext soll weitere Informationen geben, bzw. den Schritt genauer beschreiben.';
const conf_txt_help = 'Die Bestätigungsfrage wird gefragt, wenn eine Weile keine Reaktion des Nutzers kam.\nDiese sollte mit ja/nein oder fertig/nicht fertig beantwortet werden können und eine kurze Info zum Schritt beinhalten.';
const fin_txt_help = 'Der Abschlusstext wird gesagt wenn alle Schritte des Ablaufs abgeschlossen wurden.\nDer Ablauf wird danach beendet.';

// Counter for the amount of steps visible in the editor and limit constant for the amount of steps allowed.
var currentStepCount = 1;
const MAX_STEP_COUNT = 25;

// Dynamicly adds procedure-step sections into the form where the '+' button is pressed.
// The following steps are incremented (number and id) as well as the current step counter.
function addStep(btn) {
  if (currentStepCount === MAX_STEP_COUNT) {
    alert('Es kann kein Schritt mehr hinzugefügt werden.\nDie maximale Anzahl an Schritten ist ' + MAX_STEP_COUNT + '.');
    return;
  }
  var stepSection = btn.parentNode.parentNode.parentNode;
  var newStepNum = parseInt(stepSection.getElementsByTagName('SPAN')[0].innerText, 10) + 1;
  var step = '<tbody><tr><td><h5>Schritt <span>' + newStepNum + '</span>&emsp;<button type="button" name="button" onclick="removeStep(this)">-</button></h5></td></tr><tr><td><strong>Anweisungstext</strong></td><td><textarea class="instruction_text" id="inst_txt_' + newStepNum + '"></textarea></td></tr><tr><td><strong>Hilfetext</strong></td><td><textarea class="help_text" id="help_txt_' + newStepNum + '"></textarea></td></tr><tr><td><strong>Bestätigungsfrage</strong></td><td><textarea class="confirm_text" id="conf_txt_' + newStepNum + '"></textarea></td></tr><tr class="spacer_row"><td></td><td></td><td><button type="button" name="add_button" onclick="addStep(this)">+</button></td></tr></tbody>';
  var currentSection = stepSection.nextElementSibling;
  var currentNum = newStepNum + 1;
  while (currentSection != null && currentSection.id !== 'final_step') {
    renameStep(currentSection, currentNum);
    currentSection = currentSection.nextElementSibling;
    currentNum += 1;
  }
  stepSection.insertAdjacentHTML('afterend', step);
  currentStepCount++;
}

// Dynamicly removes a step section from the procedure editor form where the '-' button is pressed.
// The following steps are decremented (number and id) as well as the current step counter.
function removeStep(btn) {
  var stepSection = btn.parentNode.parentNode.parentNode.parentNode;
  var removedNum = parseInt(stepSection.getElementsByTagName('SPAN')[0].innerText, 10);
  var currentSection = stepSection.nextElementSibling;
  if (currentSection.nextElementSibling == null && stepSection.previousElementSibling.previousElementSibling == null) {
    alert('Der Schritt kann nicht entfernt werden!\nEs muss mindestens einen geben.');
    return;
  }
  var currentNum = removedNum;
  while (currentSection != null && currentSection.id !== 'final_step') {
    renameStep(currentSection, currentNum);
    currentSection = currentSection.nextElementSibling;
    currentNum += 1;
  }
  stepSection.remove();
  currentStepCount--;
}
function renameStep(section, num) {
  section.getElementsByTagName('SPAN')[0].innerText = num;
  section.getElementsByClassName('instruction_text')[0].id = 'inst_txt_' + num;
  section.getElementsByClassName('help_text')[0].id = 'help_txt_' + num;
  section.getElementsByClassName('confirm_text')[0].id = 'conf_txt_' + num;
}

// Validates the procedure form when 'save' is pressed and submits it if valid, prints error-alert otherwise.
function processProcedure() {
  var form = document.forms[0];
  if (hasEmptyFields(form)) {
    alert('Es müssen alle Felder ausgefüllt werden!\nDie Texte dürfen außerdem keine Sonderzeichen enthalten!');
    return;
  }
  var submitForm = document.forms[1];
  submitForm.elements[1].value = form.elements['name'].value;
  submitForm.elements[2].value = generateJson(form);
  submitForm.submit();
}

// Iterates through all fields of the given form and adds a red border to the respective input field if it is invalid (empty or forbidden characters).
// Returns true if everything is valid, false otherwise.
function hasEmptyFields(form) {
  var isComplete = true;
  for (var i = 0; i < form.length; i++) {
    var element = form.elements[i];
    element.removeAttribute('style');
    if (element.value === 'NEVER') { // if 'NEVER' selected, skip trigger time field
      i += 2;
      form.elements[i].removeAttribute('style');
    } else if ((element.value === '' || containsForbiddenChars(element.value)) && element.tagName !== 'BUTTON') {
      var attr = document.createAttribute('style');
      attr.value = 'border-width: 2px;border-color: red;';
      element.setAttributeNode(attr);
      isComplete = false;
    }
  }
  return !isComplete;
}
// Searches the given String for any of the specified forbidden characters and tags and returns true if there is a match, false otherwise.
function containsForbiddenChars(str) {
  var chars = ['<script>', '</script>', '<script', 'script>', '</script', '<audio>', '</audio>', '<audio', 'audio>', '</audio', '$', '{', '}', '%'];
  for (var i = 0; i < chars.length; i++) {
    if (str.includes(chars[i])) return true;
  }
  return false;
}

// Converts the given form to a valid json to comply with the procedureTemplate.json file and returns it.
function generateJson(form) {
  var e = form.elements;
  var triggers = { DAYS: e['repeat_list'].value, TIME: e['trigger_time'].value };
  var stepsArr = [];
  var stepID = 1;
  while (e['inst_txt_' + stepID] != null) {
    var step = { step: stepID, instructionText: e['inst_txt_' + stepID].value, helpText: e['help_txt_' + stepID].value, confirmText: e['conf_txt_' + stepID].value };
    stepsArr.push(step);
    stepID++;
  }
  stepsArr.push({ step: stepID, finishedText: e['fin_txt'].value });
  var obj = { procedureName: e['name'].value, triggerParameters: [triggers], steps: stepsArr };
  return JSON.stringify(obj);
}
