import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { GuardComponentsPage, GuardDeleteDialog, GuardUpdatePage } from './guard.page-object';

const expect = chai.expect;

describe('Guard e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guardComponentsPage: GuardComponentsPage;
  let guardUpdatePage: GuardUpdatePage;
  let guardDeleteDialog: GuardDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Guards', async () => {
    await navBarPage.goToEntity('guard');
    guardComponentsPage = new GuardComponentsPage();
    await browser.wait(ec.visibilityOf(guardComponentsPage.title), 5000);
    expect(await guardComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.guard.home.title');
    await browser.wait(ec.or(ec.visibilityOf(guardComponentsPage.entities), ec.visibilityOf(guardComponentsPage.noResult)), 1000);
  });

  it('should load create Guard page', async () => {
    await guardComponentsPage.clickOnCreateButton();
    guardUpdatePage = new GuardUpdatePage();
    expect(await guardUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.guard.home.createOrEditLabel');
    await guardUpdatePage.cancel();
  });

  it('should create and save Guards', async () => {
    const nbButtonsBeforeCreate = await guardComponentsPage.countDeleteButtons();

    await guardComponentsPage.clickOnCreateButton();

    await promise.all([guardUpdatePage.guardSelectLastOption()]);

    await guardUpdatePage.save();
    expect(await guardUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await guardComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Guard', async () => {
    const nbButtonsBeforeDelete = await guardComponentsPage.countDeleteButtons();
    await guardComponentsPage.clickOnLastDeleteButton();

    guardDeleteDialog = new GuardDeleteDialog();
    expect(await guardDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.guard.delete.question');
    await guardDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(guardComponentsPage.title), 5000);

    expect(await guardComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
