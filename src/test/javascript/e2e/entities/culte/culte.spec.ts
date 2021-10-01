import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CulteComponentsPage, CulteDeleteDialog, CulteUpdatePage } from './culte.page-object';

const expect = chai.expect;

describe('Culte e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let culteComponentsPage: CulteComponentsPage;
  let culteUpdatePage: CulteUpdatePage;
  let culteDeleteDialog: CulteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Cultes', async () => {
    await navBarPage.goToEntity('culte');
    culteComponentsPage = new CulteComponentsPage();
    await browser.wait(ec.visibilityOf(culteComponentsPage.title), 5000);
    expect(await culteComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.culte.home.title');
    await browser.wait(ec.or(ec.visibilityOf(culteComponentsPage.entities), ec.visibilityOf(culteComponentsPage.noResult)), 1000);
  });

  it('should load create Culte page', async () => {
    await culteComponentsPage.clickOnCreateButton();
    culteUpdatePage = new CulteUpdatePage();
    expect(await culteUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.culte.home.createOrEditLabel');
    await culteUpdatePage.cancel();
  });

  it('should create and save Cultes', async () => {
    const nbButtonsBeforeCreate = await culteComponentsPage.countDeleteButtons();

    await culteComponentsPage.clickOnCreateButton();

    await promise.all([
      culteUpdatePage.setThemeInput('theme'),
      culteUpdatePage.setDateInput('2000-12-31'),
      // culteUpdatePage.imageCulteSelectLastOption(),
    ]);

    await culteUpdatePage.save();
    expect(await culteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await culteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Culte', async () => {
    const nbButtonsBeforeDelete = await culteComponentsPage.countDeleteButtons();
    await culteComponentsPage.clickOnLastDeleteButton();

    culteDeleteDialog = new CulteDeleteDialog();
    expect(await culteDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.culte.delete.question');
    await culteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(culteComponentsPage.title), 5000);

    expect(await culteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
