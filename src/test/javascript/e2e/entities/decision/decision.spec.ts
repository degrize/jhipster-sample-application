import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DecisionComponentsPage, DecisionDeleteDialog, DecisionUpdatePage } from './decision.page-object';

const expect = chai.expect;

describe('Decision e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let decisionComponentsPage: DecisionComponentsPage;
  let decisionUpdatePage: DecisionUpdatePage;
  let decisionDeleteDialog: DecisionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Decisions', async () => {
    await navBarPage.goToEntity('decision');
    decisionComponentsPage = new DecisionComponentsPage();
    await browser.wait(ec.visibilityOf(decisionComponentsPage.title), 5000);
    expect(await decisionComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.decision.home.title');
    await browser.wait(ec.or(ec.visibilityOf(decisionComponentsPage.entities), ec.visibilityOf(decisionComponentsPage.noResult)), 1000);
  });

  it('should load create Decision page', async () => {
    await decisionComponentsPage.clickOnCreateButton();
    decisionUpdatePage = new DecisionUpdatePage();
    expect(await decisionUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.decision.home.createOrEditLabel');
    await decisionUpdatePage.cancel();
  });

  it('should create and save Decisions', async () => {
    const nbButtonsBeforeCreate = await decisionComponentsPage.countDeleteButtons();

    await decisionComponentsPage.clickOnCreateButton();

    await promise.all([decisionUpdatePage.decisionSelectLastOption()]);

    await decisionUpdatePage.save();
    expect(await decisionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await decisionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Decision', async () => {
    const nbButtonsBeforeDelete = await decisionComponentsPage.countDeleteButtons();
    await decisionComponentsPage.clickOnLastDeleteButton();

    decisionDeleteDialog = new DecisionDeleteDialog();
    expect(await decisionDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.decision.delete.question');
    await decisionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(decisionComponentsPage.title), 5000);

    expect(await decisionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
