// javascript shared across modal pages, to enforce modal-ness, so this file is included by
// the modal page level decorator, modal.jsp

// set the event handler so the user will be prompted as to whether they really want to 
// leave the page without having clicked on an action button (such as Save)
window.onbeforeunload = confirmExit;

// the following variables exist to affect whether the prompt to confirm exit will be
// issued or not

// the quicklink flag prevents prompting user to confirm exit when going to a quicklink
// this is set true as part of the quicklink javascript (sectionQuicklink.tag)
// if there are no quicklinks on the page, this variable remains false and does no harm
var quicklink = false;

// there are some views that are presented with the modal decorator that do not have data
// entry, such that the modal-ness does not need to be enforced because there is no need
// to make sure the user has saved their work prior to leaving the page. the reason these
// pages use the modal decorator is either because they need more screen real estate and the 
// modal decorator gives this (e.g. it does not have the left nav area), or they are part of a 
// flow conversation where the other pages need the modal decorator. since these views
// are not concerned with enforcement of modal-ness, the confirmExit flag should check the
// componentView and prevent the confirm dialog from displaying when the user leaves the page.
// the componentView is passed from the content decorator to the modal decorator which sets
// the following variable, which is then used in the confirmExit function
var componentView = "";

// note that the "submitted" variable is also used to affect the confirm exit prompt, but it
// must be declared in common.js which is included by all pages because it is set in the
// submitForm function which lives in common.js (because even non-modal pages call submitForm
// e.g. clearing patient/project context, list filtering, etc..


// the event handler prompts that user, asking them if they really want to leave the page
// without saving, unless the user has clicked on an action button, such as Save, which 
// submits the page, or the user has clicked on a quicklink which just moves them to another
// part of the page
function confirmExit() {
  // 'view' and 'status' (which is the instrument status view) do not have editable data, so 
  // do not prompt user
  if (!submitted && !quicklink && componentView != 'view' && componentView != 'status') {
      return "Are you sure you want to leave this page without saving ?";
  }
  // if user clicked on a quicklink thereby setting the quicklink flag, need to turn it back
  // off so it will not prevent the confirm exit dialog 
  quicklink = false;
}


