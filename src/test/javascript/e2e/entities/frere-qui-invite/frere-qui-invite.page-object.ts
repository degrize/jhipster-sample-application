import { element, by, ElementFinder } from 'protractor';

export class FrereQuiInviteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-frere-qui-invite div table .btn-danger'));
  title = element.all(by.css('jhi-frere-qui-invite div h2#page-heading span')).first();
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

export class FrereQuiInviteUpdatePage {
  pageTitle = element(by.id('jhi-frere-qui-invite-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomCompletInput = element(by.id('field_nomComplet'));
  contactInput = element(by.id('field_contact'));
  sexeSelect = element(by.id('field_sexe'));

  quartierSelect = element(by.id('field_quartier'));
  departementSelect = element(by.id('field_departement'));

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

  async setSexeSelect(sexe: string): Promise<void> {
    await this.sexeSelect.sendKeys(sexe);
  }

  async getSexeSelect(): Promise<string> {
    return await this.sexeSelect.element(by.css('option:checked')).getText();
  }

  async sexeSelectLastOption(): Promise<void> {
    await this.sexeSelect.all(by.tagName('option')).last().click();
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

export class FrereQuiInviteDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-frereQuiInvite-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-frereQuiInvite'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
