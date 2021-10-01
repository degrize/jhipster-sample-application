import { element, by, ElementFinder } from 'protractor';

export class GuardComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-guard div table .btn-danger'));
  title = element.all(by.css('jhi-guard div h2#page-heading span')).first();
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

export class GuardUpdatePage {
  pageTitle = element(by.id('jhi-guard-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));

  guardSelect = element(by.id('field_guard'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
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

export class GuardDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-guard-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-guard'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
