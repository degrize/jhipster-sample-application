import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ImageCulteComponentsPage, ImageCulteDeleteDialog, ImageCulteUpdatePage } from './image-culte.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('ImageCulte e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let imageCulteComponentsPage: ImageCulteComponentsPage;
  let imageCulteUpdatePage: ImageCulteUpdatePage;
  let imageCulteDeleteDialog: ImageCulteDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ImageCultes', async () => {
    await navBarPage.goToEntity('image-culte');
    imageCulteComponentsPage = new ImageCulteComponentsPage();
    await browser.wait(ec.visibilityOf(imageCulteComponentsPage.title), 5000);
    expect(await imageCulteComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.imageCulte.home.title');
    await browser.wait(ec.or(ec.visibilityOf(imageCulteComponentsPage.entities), ec.visibilityOf(imageCulteComponentsPage.noResult)), 1000);
  });

  it('should load create ImageCulte page', async () => {
    await imageCulteComponentsPage.clickOnCreateButton();
    imageCulteUpdatePage = new ImageCulteUpdatePage();
    expect(await imageCulteUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.imageCulte.home.createOrEditLabel');
    await imageCulteUpdatePage.cancel();
  });

  it('should create and save ImageCultes', async () => {
    const nbButtonsBeforeCreate = await imageCulteComponentsPage.countDeleteButtons();

    await imageCulteComponentsPage.clickOnCreateButton();

    await promise.all([imageCulteUpdatePage.setTitreInput('titre'), imageCulteUpdatePage.setImageInput(absolutePath)]);

    await imageCulteUpdatePage.save();
    expect(await imageCulteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await imageCulteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ImageCulte', async () => {
    const nbButtonsBeforeDelete = await imageCulteComponentsPage.countDeleteButtons();
    await imageCulteComponentsPage.clickOnLastDeleteButton();

    imageCulteDeleteDialog = new ImageCulteDeleteDialog();
    expect(await imageCulteDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.imageCulte.delete.question');
    await imageCulteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(imageCulteComponentsPage.title), 5000);

    expect(await imageCulteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
