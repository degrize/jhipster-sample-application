import { element, by, ElementFinder } from 'protractor';

export class CulteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-culte div table .btn-danger'));
  title = element.all(by.css('jhi-culte div h2#page-heading span')).first();
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

export class CulteUpdatePage {
  pageTitle = element(by.id('jhi-culte-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  themeInput = element(by.id('field_theme'));
  dateInput = element(by.id('field_date'));

  imageCulteSelect = element(by.id('field_imageCulte'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setThemeInput(theme: string): Promise<void> {
    await this.themeInput.sendKeys(theme);
  }

  async getThemeInput(): Promise<string> {
    return await this.themeInput.getAttribute('value');
  }

  async setDateInput(date: string): Promise<void> {
    await this.dateInput.sendKeys(date);
  }

  async getDateInput(): Promise<string> {
    return await this.dateInput.getAttribute('value');
  }

  async imageCulteSelectLastOption(): Promise<void> {
    await this.imageCulteSelect.all(by.tagName('option')).last().click();
  }

  async imageCulteSelectOption(option: string): Promise<void> {
    await this.imageCulteSelect.sendKeys(option);
  }

  getImageCulteSelect(): ElementFinder {
    return this.imageCulteSelect;
  }

  async getImageCulteSelectedOption(): Promise<string> {
    return await this.imageCulteSelect.element(by.css('option:checked')).getText();
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

export class CulteDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-culte-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-culte'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
