import { element, by, ElementFinder } from 'protractor';

export class NouveauComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-nouveau div table .btn-danger'));
  title = element.all(by.css('jhi-nouveau div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class NouveauUpdatePage {
  pageTitle = element(by.id('jhi-nouveau-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomCompletInput = element(by.id('field_nomComplet'));
  contactInput = element(by.id('field_contact'));
  trancheAgeInput = element(by.id('field_trancheAge'));
  situationMatrimonialeSelect = element(by.id('field_situationMatrimoniale'));
  dateInput = element(by.id('field_date'));
  impressionsDuCulteInput = element(by.id('field_impressionsDuCulte'));
  sexeSelect = element(by.id('field_sexe'));
  inviteParSelect = element(by.id('field_invitePar'));

  communauteSelect = element(by.id('field_communaute'));
  villeSelect = element(by.id('field_ville'));
  quartierSelect = element(by.id('field_quartier'));
  culteSelect = element(by.id('field_culte'));
  departementSelect = element(by.id('field_departement'));
  frereQuiInviteSelect = element(by.id('field_frereQuiInvite'));
  besoinSelect = element(by.id('field_besoin'));
  decisionSelect = element(by.id('field_decision'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomCompletInput(nomComplet: string): Promise<void> {
    await this.nomCompletInput.sendKeys(nomComplet);
  }

  async getNomCompletInput(): Promise<string> {
    return await this.nomCompletInput.getAttribute('value');
  }

  async setContactInput(contact: string): Promise<void> {
    await this.contactInput.sendKeys(contact);
  }

  async getContactInput(): Promise<string> {
    return await this.contactInput.getAttribute('value');
  }

  async setTrancheAgeInput(trancheAge: string): Promise<void> {
    await this.trancheAgeInput.sendKeys(trancheAge);
  }

  async getTrancheAgeInput(): Promise<string> {
    return await this.trancheAgeInput.getAttribute('value');
  }

  async setSituationMatrimonialeSelect(situationMatrimoniale: string): Promise<void> {
    await this.situationMatrimonialeSelect.sendKeys(situationMatrimoniale);
  }

  async getSituationMatrimonialeSelect(): Promise<string> {
    return await this.situationMatrimonialeSelect.element(by.css('option:checked')).getText();
  }

  async situationMatrimonialeSelectLastOption(): Promise<void> {
    await this.situationMatrimonialeSelect.all(by.tagName('option')).last().click();
  }

  async setDateInput(date: string): Promise<void> {
    await this.dateInput.sendKeys(date);
  }

  async getDateInput(): Promise<string> {
    return await this.dateInput.getAttribute('value');
  }

  async setImpressionsDuCulteInput(impressionsDuCulte: string): Promise<void> {
    await this.impressionsDuCulteInput.sendKeys(impressionsDuCulte);
  }

  async getImpressionsDuCulteInput(): Promise<string> {
    return await this.impressionsDuCulteInput.getAttribute('value');
  }

  async setSexeSelect(sexe: string): Promise<void> {
    await this.sexeSelect.sendKeys(sexe);
  }

  async getSexeSelect(): Promise<string> {
    return await this.sexeSelect.element(by.css('option:checked')).getText();
  }

  async sexeSelectLastOption(): Promise<void> {
    await this.sexeSelect.all(by.tagName('option')).last().click();
  }

  async setInviteParSelect(invitePar: string): Promise<void> {
    await this.inviteParSelect.sendKeys(invitePar);
  }

  async getInviteParSelect(): Promise<string> {
    return await this.inviteParSelect.element(by.css('option:checked')).getText();
  }

  async inviteParSelectLastOption(): Promise<void> {
    await this.inviteParSelect.all(by.tagName('option')).last().click();
  }

  async communauteSelectLastOption(): Promise<void> {
    await this.communauteSelect.all(by.tagName('option')).last().click();
  }

  async communauteSelectOption(option: string): Promise<void> {
    await this.communauteSelect.sendKeys(option);
  }

  getCommunauteSelect(): ElementFinder {
    return this.communauteSelect;
  }

  async getCommunauteSelectedOption(): Promise<string> {
    return await this.communauteSelect.element(by.css('option:checked')).getText();
  }

  async villeSelectLastOption(): Promise<void> {
    await this.villeSelect.all(by.tagName('option')).last().click();
  }

  async villeSelectOption(option: string): Promise<void> {
    await this.villeSelect.sendKeys(option);
  }

  getVilleSelect(): ElementFinder {
    return this.villeSelect;
  }

  async getVilleSelectedOption(): Promise<string> {
    return await this.villeSelect.element(by.css('option:checked')).getText();
  }

  async quartierSelectLastOption(): Promise<void> {
    await this.quartierSelect.all(by.tagName('option')).last().click();
  }

  async quartierSelectOption(option: string): Promise<void> {
    await this.quartierSelect.sendKeys(option);
  }

  getQuartierSelect(): ElementFinder {
    return this.quartierSelect;
  }

  async getQuartierSelectedOption(): Promise<string> {
    return await this.quartierSelect.element(by.css('option:checked')).getText();
  }

  async culteSelectLastOption(): Promise<void> {
    await this.culteSelect.all(by.tagName('option')).last().click();
  }

  async culteSelectOption(option: string): Promise<void> {
    await this.culteSelect.sendKeys(option);
  }

  getCulteSelect(): ElementFinder {
    return this.culteSelect;
  }

  async getCulteSelectedOption(): Promise<string> {
    return await this.culteSelect.element(by.css('option:checked')).getText();
  }

  async departementSelectLastOption(): Promise<void> {
    await this.departementSelect.all(by.tagName('option')).last().click();
  }

  async departementSelectOption(option: string): Promise<void> {
    await this.departementSelect.sendKeys(option);
  }

  getDepartementSelect(): ElementFinder {
    return this.departementSelect;
  }

  async getDepartementSelectedOption(): Promise<string> {
    return await this.departementSelect.element(by.css('option:checked')).getText();
  }

  async frereQuiInviteSelectLastOption(): Promise<void> {
    await this.frereQuiInviteSelect.all(by.tagName('option')).last().click();
  }

  async frereQuiInviteSelectOption(option: string): Promise<void> {
    await this.frereQuiInviteSelect.sendKeys(option);
  }

  getFrereQuiInviteSelect(): ElementFinder {
    return this.frereQuiInviteSelect;
  }

  async getFrereQuiInviteSelectedOption(): Promise<string> {
    return await this.frereQuiInviteSelect.element(by.css('option:checked')).getText();
  }

  async besoinSelectLastOption(): Promise<void> {
    await this.besoinSelect.all(by.tagName('option')).last().click();
  }

  async besoinSelectOption(option: string): Promise<void> {
    await this.besoinSelect.sendKeys(option);
  }

  getBesoinSelect(): ElementFinder {
    return this.besoinSelect;
  }

  async getBesoinSelectedOption(): Promise<string> {
    return await this.besoinSelect.element(by.css('option:checked')).getText();
  }

  async decisionSelectLastOption(): Promise<void> {
    await this.decisionSelect.all(by.tagName('option')).last().click();
  }

  async decisionSelectOption(option: string): Promise<void> {
    await this.decisionSelect.sendKeys(option);
  }

  getDecisionSelect(): ElementFinder {
    return this.decisionSelect;
  }

  async getDecisionSelectedOption(): Promise<string> {
    return await this.decisionSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class NouveauDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-nouveau-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-nouveau'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
