import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DepartementComponentsPage, DepartementDeleteDialog, DepartementUpdatePage } from './departement.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Departement e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let departementComponentsPage: DepartementComponentsPage;
  let departementUpdatePage: DepartementUpdatePage;
  let departementDeleteDialog: DepartementDeleteDialog;
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

  it('should load Departements', async () => {
    await navBarPage.goToEntity('departement');
    departementComponentsPage = new DepartementComponentsPage();
    await browser.wait(ec.visibilityOf(departementComponentsPage.title), 5000);
    expect(await departementComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.departement.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(departementComponentsPage.entities), ec.visibilityOf(departementComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Departement page', async () => {
    await departementComponentsPage.clickOnCreateButton();
    departementUpdatePage = new DepartementUpdatePage();
    expect(await departementUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.departement.home.createOrEditLabel');
    await departementUpdatePage.cancel();
  });

  it('should create and save Departements', async () => {
    const nbButtonsBeforeCreate = await departementComponentsPage.countDeleteButtons();

    await departementComponentsPage.clickOnCreateButton();

    await promise.all([
      departementUpdatePage.setNomInput('nom'),
      departementUpdatePage.setShortNameInput('shortName'),
      departementUpdatePage.setNomResponsableInput('nomResponsable'),
      departementUpdatePage.setVideoIntroductionInput(absolutePath),
      departementUpdatePage.setContactResponsableInput('contactResponsable'),
      departementUpdatePage.setDescriptionInput('description'),
      departementUpdatePage.setCouleur1Input('couleur1'),
      departementUpdatePage.setCouleur2Input('couleur2'),
      // departementUpdatePage.imageCulteSelectLastOption(),
    ]);

    await departementUpdatePage.save();
    expect(await departementUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await departementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Departement', async () => {
    const nbButtonsBeforeDelete = await departementComponentsPage.countDeleteButtons();
    await departementComponentsPage.clickOnLastDeleteButton();

    departementDeleteDialog = new DepartementDeleteDialog();
    expect(await departementDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.departement.delete.question');
    await departementDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(departementComponentsPage.title), 5000);

    expect(await departementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
