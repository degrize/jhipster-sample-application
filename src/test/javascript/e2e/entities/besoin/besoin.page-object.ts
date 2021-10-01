import { element, by, ElementFinder } from 'protractor';

export class BesoinComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-besoin div table .btn-danger'));
  title = element.all(by.css('jhi-besoin div h2#page-heading span')).first();
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

export class BesoinUpdatePage {
  pageTitle = element(by.id('jhi-besoin-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  besoinSelect = element(by.id('field_besoin'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setBesoinSelect(besoin: string): Promise<void> {
    await this.besoinSelect.sendKeys(besoin);
  }

  async getBesoinSelect(): Promise<string> {
    return await this.besoinSelect.element(by.css('option:checked')).getText();
  }

  async besoinSelectLastOption(): Promise<void> {
    await this.besoinSelect.all(by.tagName('option')).last().click();
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

export class BesoinDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-besoin-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-besoin'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
