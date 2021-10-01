import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FrereQuiInviteComponentsPage, FrereQuiInviteDeleteDialog, FrereQuiInviteUpdatePage } from './frere-qui-invite.page-object';

const expect = chai.expect;

describe('FrereQuiInvite e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let frereQuiInviteComponentsPage: FrereQuiInviteComponentsPage;
  let frereQuiInviteUpdatePage: FrereQuiInviteUpdatePage;
  let frereQuiInviteDeleteDialog: FrereQuiInviteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FrereQuiInvites', async () => {
    await navBarPage.goToEntity('frere-qui-invite');
    frereQuiInviteComponentsPage = new FrereQuiInviteComponentsPage();
    await browser.wait(ec.visibilityOf(frereQuiInviteComponentsPage.title), 5000);
    expect(await frereQuiInviteComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.frereQuiInvite.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(frereQuiInviteComponentsPage.entities), ec.visibilityOf(frereQuiInviteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FrereQuiInvite page', async () => {
    await frereQuiInviteComponentsPage.clickOnCreateButton();
    frereQuiInviteUpdatePage = new FrereQuiInviteUpdatePage();
    expect(await frereQuiInviteUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.frereQuiInvite.home.createOrEditLabel');
    await frereQuiInviteUpdatePage.cancel();
  });

  it('should create and save FrereQuiInvites', async () => {
    const nbButtonsBeforeCreate = await frereQuiInviteComponentsPage.countDeleteButtons();

    await frereQuiInviteComponentsPage.clickOnCreateButton();

    await promise.all([
      frereQuiInviteUpdatePage.setNomCompletInput('nomComplet'),
      frereQuiInviteUpdatePage.setContactInput('contact'),
      frereQuiInviteUpdatePage.sexeSelectLastOption(),
      frereQuiInviteUpdatePage.quartierSelectLastOption(),
      // frereQuiInviteUpdatePage.departementSelectLastOption(),
    ]);

    await frereQuiInviteUpdatePage.save();
    expect(await frereQuiInviteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await frereQuiInviteComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FrereQuiInvite', async () => {
    const nbButtonsBeforeDelete = await frereQuiInviteComponentsPage.countDeleteButtons();
    await frereQuiInviteComponentsPage.clickOnLastDeleteButton();

    frereQuiInviteDeleteDialog = new FrereQuiInviteDeleteDialog();
    expect(await frereQuiInviteDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.frereQuiInvite.delete.question');
    await frereQuiInviteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(frereQuiInviteComponentsPage.title), 5000);

    expect(await frereQuiInviteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
