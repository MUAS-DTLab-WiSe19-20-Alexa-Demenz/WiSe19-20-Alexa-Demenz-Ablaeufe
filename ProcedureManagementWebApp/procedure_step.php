<tbody>
  <tr>
    <td><h5>Schritt <span><?php echo $stepNum; ?></span>&emsp;<button type="button" name="button" title="Schritt entfernen" onclick="removeStep(this)">-</button></h5></td>
  </tr>
  <tr>
    <td><strong>Anweisungstext</strong></td><td><textarea class="instruction_text" id="inst_txt_<?php echo $stepNum;?>"><?php echo $instrTxt;?></textarea></td>
    <td><button type="button" class="help_btn" onclick="alert(instr_txt_help)">?</button></td>
  </tr>
  <tr>
    <td><strong>Hilfetext</strong></td><td><textarea class="help_text" id="help_txt_<?php echo $stepNum;?>"><?php echo $helpTxt;?></textarea></td>
    <td><button type="button" class="help_btn" onclick="alert(help_txt_help)">?</button></td>
  </tr>
  <tr>
    <td><strong>Bestätigungsfrage</strong></td><td><textarea class="confirm_text" id="conf_txt_<?php echo $stepNum;?>"><?php echo $confTxt;?></textarea></td>
    <td><button type="button" class="help_btn" onclick="alert(conf_txt_help)">?</button></td>
  </tr>
  <tr class="spacer_row">
    <td></td><td></td><td><button type="button" name="add_button" title="Schritt hinzufügen" onclick="addStep(this)">+</button></td>
  </tr>
</tbody>
