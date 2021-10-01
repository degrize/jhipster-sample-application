import { element, by, ElementFinder } from 'protractor';

export class GemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-gem div table .btn-danger'));
  title = element.all(by.css('jhi-gem div h2#page-heading span')).first();
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

export class GemUpdatePage {
  pageTitle = element(by.id('jhi-gem-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomInput = element(by.id('field_nom'));
  anneeInput = element(by.id('field_annee'));

  guardSelect = element(by.id('field_guard'));
  departementSelect = element(by.id('field_departement'));
  frereQuiInviteSelect = element(by.id('field_frereQuiInvite'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomInput(nom: string): Promise<void> {
    await this.nomInput.sendKeys(nom);
  }

  async getNomInput(): Promise<string> {
    return await this.nomInput.getAttribute('value');
  }

  async setAnneeInput(annee: string): Promise<void> {
    await this.anneeInput.sendKeys(annee);
  }

  async getAnneeInput(): Promise<string> {
    return await this.anneeInput.getAttribute('value');
  }

  async guardSelectLastOption(): Promise<void> {
    await this.guardSelect.all(by.tagName('option')).last().click();
  }

  async guardSelectOption(option: string): Promise<void> {
    await this.guardSelect.sendKeys(option);
  }

  getGuardSelect(): ElementFinder {
    return this.guardSelect;
  }

  async getGuardSelectedOption(): Promise<string> {
    return await this.guardSelect.element(by.css('option:checked')).getText();
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

export class GemDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-gem-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-gem'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
