import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { NouveauComponentsPage, NouveauDeleteDialog, NouveauUpdatePage } from './nouveau.page-object';

const expect = chai.expect;

describe('Nouveau e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let nouveauComponentsPage: NouveauComponentsPage;
  let nouveauUpdatePage: NouveauUpdatePage;
  let nouveauDeleteDialog: NouveauDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Nouveaus', async () => {
    await navBarPage.goToEntity('nouveau');
    nouveauComponentsPage = new NouveauComponentsPage();
    await browser.wait(ec.visibilityOf(nouveauComponentsPage.title), 5000);
    expect(await nouveauComponentsPage.getTitle()).to.eq('jhipsterSampleApplicationApp.nouveau.home.title');
    await browser.wait(ec.or(ec.visibilityOf(nouveauComponentsPage.entities), ec.visibilityOf(nouveauComponentsPage.noResult)), 1000);
  });

  it('should load create Nouveau page', async () => {
    await nouveauComponentsPage.clickOnCreateButton();
    nouveauUpdatePage = new NouveauUpdatePage();
    expect(await nouveauUpdatePage.getPageTitle()).to.eq('jhipsterSampleApplicationApp.nouveau.home.createOrEditLabel');
    await nouveauUpdatePage.cancel();
  });

  it('should create and save Nouveaus', async () => {
    const nbButtonsBeforeCreate = await nouveauComponentsPage.countDeleteButtons();

    await nouveauComponentsPage.clickOnCreateButton();

    await promise.all([
      nouveauUpdatePage.setNomCompletInput('nomComplet'),
      nouveauUpdatePage.setContactInput('contact'),
      nouveauUpdatePage.setTrancheAgeInput('trancheAge'),
      nouveauUpdatePage.situationMatrimonialeSelectLastOption(),
      nouveauUpdatePage.setDateInput('2000-12-31'),
      nouveauUpdatePage.setImpressionsDuCulteInput('impressionsDuCulte'),
      nouveauUpdatePage.sexeSelectLastOption(),
      nouveauUpdatePage.inviteParSelectLastOption(),
      nouveauUpdatePage.communauteSelectLastOption(),
      nouveauUpdatePage.villeSelectLastOption(),
      nouveauUpdatePage.quartierSelectLastOption(),
      nouveauUpdatePage.culteSelectLastOption(),
      // nouveauUpdatePage.departementSelectLastOption(),
      // nouveauUpdatePage.frereQuiInviteSelectLastOption(),
      // nouveauUpdatePage.besoinSelectLastOption(),
      // nouveauUpdatePage.decisionSelectLastOption(),
    ]);

    await nouveauUpdatePage.save();
    expect(await nouveauUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await nouveauComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Nouveau', async () => {
    const nbButtonsBeforeDelete = await nouveauComponentsPage.countDeleteButtons();
    await nouveauComponentsPage.clickOnLastDeleteButton();

    nouveauDeleteDialog = new NouveauDeleteDialog();
    expect(await nouveauDeleteDialog.getDialogTitle()).to.eq('jhipsterSampleApplicationApp.nouveau.delete.question');
    await nouveauDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(nouveauComponentsPage.title), 5000);

    expect(await nouveauComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
