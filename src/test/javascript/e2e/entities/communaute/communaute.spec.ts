import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CommunauteComponentsPage, CommunauteDeleteDialog, CommunauteUpdatePage } from './communaute.page-object';

const expect = chai.expect;

describe('Communaute e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let communauteComponentsPage: CommunauteComponentsPage;
  let communauteUpdatePage: CommunauteUpdatePage;
  let communauteDeleteDialog: CommunauteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Communautes', async () => {
    await navBarPage.goToEntity('communaute');
    communauteComponentsPage = new CommunauteComponentsPage();
    await browser.wait(ec.visibilityOf(communauteComponentsPage.title), 5000);
    expect(await communauteComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.communaute.home.title');
    await browser.wait(ec.or(ec.visibilityOf(communauteComponentsPage.entities), ec.visibilityOf(communauteComponentsPage.noResult)), 1000);
  });

  it('should load create Communaute page', async () => {
    await communauteComponentsPage.clickOnCreateButton();
    communauteUpdatePage = new CommunauteUpdatePage();
    expect(await communauteUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.communaute.home.createOrEditLabel');
    await communauteUpdatePage.cancel();
  });

  it('should create and save Communautes', async () => {
    const nbButtonsBeforeCreate = await communauteComponentsPage.countDeleteButtons();

    await communauteComponentsPage.clickOnCreateButton();

    await promise.all([communauteUpdatePage.setNomInput('nom')]);

    await communauteUpdatePage.save();
    expect(await communauteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await communauteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Communaute', async () => {
    const nbButtonsBeforeDelete = await communauteComponentsPage.countDeleteButtons();
    await communauteComponentsPage.clickOnLastDeleteButton();

    communauteDeleteDialog = new CommunauteDeleteDialog();
    expect(await communauteDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.communaute.delete.question');
    await communauteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(communauteComponentsPage.title), 5000);

    expect(await communauteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
