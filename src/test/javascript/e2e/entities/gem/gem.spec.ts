import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { GemComponentsPage, GemDeleteDialog, GemUpdatePage } from './gem.page-object';

const expect = chai.expect;

describe('Gem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let gemComponentsPage: GemComponentsPage;
  let gemUpdatePage: GemUpdatePage;
  let gemDeleteDialog: GemDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Gems', async () => {
    await navBarPage.goToEntity('gem');
    gemComponentsPage = new GemComponentsPage();
    await browser.wait(ec.visibilityOf(gemComponentsPage.title), 5000);
    expect(await gemComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.gem.home.title');
    await browser.wait(ec.or(ec.visibilityOf(gemComponentsPage.entities), ec.visibilityOf(gemComponentsPage.noResult)), 1000);
  });

  it('should load create Gem page', async () => {
    await gemComponentsPage.clickOnCreateButton();
    gemUpdatePage = new GemUpdatePage();
    expect(await gemUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.gem.home.createOrEditLabel');
    await gemUpdatePage.cancel();
  });

  it('should create and save Gems', async () => {
    const nbButtonsBeforeCreate = await gemComponentsPage.countDeleteButtons();

    await gemComponentsPage.clickOnCreateButton();

    await promise.all([
      gemUpdatePage.setNomInput('nom'),
      gemUpdatePage.setAnneeInput('annee'),
      gemUpdatePage.guardSelectLastOption(),
      gemUpdatePage.departementSelectLastOption(),
      // gemUpdatePage.frereQuiInviteSelectLastOption(),
    ]);

    await gemUpdatePage.save();
    expect(await gemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await gemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Gem', async () => {
    const nbButtonsBeforeDelete = await gemComponentsPage.countDeleteButtons();
    await gemComponentsPage.clickOnLastDeleteButton();

    gemDeleteDialog = new GemDeleteDialog();
    expect(await gemDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.gem.delete.question');
    await gemDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(gemComponentsPage.title), 5000);

    expect(await gemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
