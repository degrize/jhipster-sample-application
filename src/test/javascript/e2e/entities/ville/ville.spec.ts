import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { VilleComponentsPage, VilleDeleteDialog, VilleUpdatePage } from './ville.page-object';

const expect = chai.expect;

describe('Ville e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let villeComponentsPage: VilleComponentsPage;
  let villeUpdatePage: VilleUpdatePage;
  let villeDeleteDialog: VilleDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Villes', async () => {
    await navBarPage.goToEntity('ville');
    villeComponentsPage = new VilleComponentsPage();
    await browser.wait(ec.visibilityOf(villeComponentsPage.title), 5000);
    expect(await villeComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.ville.home.title');
    await browser.wait(ec.or(ec.visibilityOf(villeComponentsPage.entities), ec.visibilityOf(villeComponentsPage.noResult)), 1000);
  });

  it('should load create Ville page', async () => {
    await villeComponentsPage.clickOnCreateButton();
    villeUpdatePage = new VilleUpdatePage();
    expect(await villeUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.ville.home.createOrEditLabel');
    await villeUpdatePage.cancel();
  });

  it('should create and save Villes', async () => {
    const nbButtonsBeforeCreate = await villeComponentsPage.countDeleteButtons();

    await villeComponentsPage.clickOnCreateButton();

    await promise.all([villeUpdatePage.setNomInput('nom')]);

    await villeUpdatePage.save();
    expect(await villeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await villeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Ville', async () => {
    const nbButtonsBeforeDelete = await villeComponentsPage.countDeleteButtons();
    await villeComponentsPage.clickOnLastDeleteButton();

    villeDeleteDialog = new VilleDeleteDialog();
    expect(await villeDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.ville.delete.question');
    await villeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(villeComponentsPage.title), 5000);

    expect(await villeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
