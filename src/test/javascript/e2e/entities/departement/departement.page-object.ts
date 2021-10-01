import { element, by, ElementFinder } from 'protractor';

export class DepartementComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-departement div table .btn-danger'));
  title = element.all(by.css('jhi-departement div h2#page-heading span')).first();
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

export class DepartementUpdatePage {
  pageTitle = element(by.id('jhi-departement-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomInput = element(by.id('field_nom'));
  shortNameInput = element(by.id('field_shortName'));
  nomResponsableInput = element(by.id('field_nomResponsable'));
  videoIntroductionInput = element(by.id('file_videoIntroduction'));
  contactResponsableInput = element(by.id('field_contactResponsable'));
  descriptionInput = element(by.id('field_description'));
  couleur1Input = element(by.id('field_couleur1'));
  couleur2Input = element(by.id('field_couleur2'));

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

  async setNomInput(nom: string): Promise<void> {
    await this.nomInput.sendKeys(nom);
  }

  async getNomInput(): Promise<string> {
    return await this.nomInput.getAttribute('value');
  }

  async setShortNameInput(shortName: string): Promise<void> {
    await this.shortNameInput.sendKeys(shortName);
  }

  async getShortNameInput(): Promise<string> {
    return await this.shortNameInput.getAttribute('value');
  }

  async setNomResponsableInput(nomResponsable: string): Promise<void> {
    await this.nomResponsableInput.sendKeys(nomResponsable);
  }

  async getNomResponsableInput(): Promise<string> {
    return await this.nomResponsableInput.getAttribute('value');
  }

  async setVideoIntroductionInput(videoIntroduction: string): Promise<void> {
    await this.videoIntroductionInput.sendKeys(videoIntroduction);
  }

  async getVideoIntroductionInput(): Promise<string> {
    return await this.videoIntroductionInput.getAttribute('value');
  }

  async setContactResponsableInput(contactResponsable: string): Promise<void> {
    await this.contactResponsableInput.sendKeys(contactResponsable);
  }

  async getContactResponsableInput(): Promise<string> {
    return await this.contactResponsableInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setCouleur1Input(couleur1: string): Promise<void> {
    await this.couleur1Input.sendKeys(couleur1);
  }

  async getCouleur1Input(): Promise<string> {
    return await this.couleur1Input.getAttribute('value');
  }

  async setCouleur2Input(couleur2: string): Promise<void> {
    await this.couleur2Input.sendKeys(couleur2);
  }

  async getCouleur2Input(): Promise<string> {
    return await this.couleur2Input.getAttribute('value');
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

export class DepartementDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-departement-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-departement'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
