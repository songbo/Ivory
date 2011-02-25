package ivory.regression;

import ivory.eval.Qrels;
import ivory.regression.GroundTruth.Metric;
import ivory.smrf.retrieval.Accumulator;
import ivory.smrf.retrieval.BatchQueryRunner;

import java.util.HashMap;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;
import org.junit.Test;

import edu.umd.cloud9.collection.DocnoMapping;


/* Note: different metrics are optimized separately */

public class Wt10g_CIKM2010_Title_Indep {

	private static final Logger sLogger = Logger.getLogger(Wt10g_CIKM2010_Title_Indep.class);

	private static String[] x10_rawAP = new String[] { "501", "0.3349", "502", "0.1483", "503",
			"0.0221", "504", "0.4367", "505", "0.3291", "506", "0.1699", "507", "0.0049", "508",
			"0.1754", "509", "0.2914", "510", "0.3288", "511", "0.3225", "512", "0.1451", "513",
			"0.0965", "514", "0.1164", "515", "0.0307", "516", "0.0948", "517", "0.0455", "518",
			"0.1720", "519", "0.1274", "520", "0.0842", "521", "0.0013", "522", "0.0481", "523",
			"0.4681", "524", "0.0001", "525", "0.0164", "526", "0.0961", "527", "0.1792", "528",
			"0.3797", "529", "0.3852", "530", "0.6676", "531", "0.0793", "532", "0.1984", "533",
			"0.1675", "534", "0.0006", "535", "0.0004", "536", "0.1055", "537", "0.0008", "538",
			"0.3250", "539", "0.0458", "540", "0.1067", "541", "0.0195", "542", "0.0102", "543",
			"0.0007", "544", "0.6037", "545", "0.2045", "546", "0.0908", "547", "0.2261", "548",
			"0.2250", "549", "0.2351", "550", "0.1126" };

	private static String[] x15_rawAP = new String[] { "501", "0.4148", "502", "0.1085", "503",
			"0.1566", "504", "0.4969", "505", "0.3959", "506", "0.1278", "507", "0.2717", "508",
			"0.1862", "509", "0.3846", "510", "0.5547", "511", "0.2931", "512", "0.1847", "513",
			"0.0965", "514", "0.2372", "515", "0.0416", "516", "0.0948", "517", "0.0337", "518",
			"0.2260", "519", "0.1345", "520", "0.0995", "521", "0.0181", "522", "0.1293", "523",
			"0.4681", "524", "0.0523", "525", "0.0641", "526", "0.0961", "527", "0.3054", "528",
			"0.3853", "529", "0.4438", "530", "0.6793", "531", "0.0893", "532", "0.1984", "533",
			"0.2091", "534", "0.0094", "535", "0.0011", "536", "0.1433", "537", "0.0034", "538",
			"0.3250", "539", "0.1163", "540", "0.0876", "541", "0.0117", "542", "0.0354", "543",
			"0.0009", "544", "0.6303", "545", "0.1004", "546", "0.1073", "547", "0.2239", "548",
			"0.8333", "549", "0.3109", "550", "0.0992" };

	private static String[] x20_rawAP = new String[] { "501", "0.3932", "502", "0.0773", "503",
			"0.1626", "504", "0.4716", "505", "0.3523", "506", "0.1027", "507", "0.3420", "508",
			"0.2583", "509", "0.3990", "510", "0.6004", "511", "0.3630", "512", "0.1851", "513",
			"0.0955", "514", "0.1982", "515", "0.1962", "516", "0.0945", "517", "0.0373", "518",
			"0.2201", "519", "0.1244", "520", "0.1156", "521", "0.0207", "522", "0.2207", "523",
			"0.4668", "524", "0.0896", "525", "0.1190", "526", "0.0963", "527", "0.3581", "528",
			"0.4589", "529", "0.4014", "530", "0.6808", "531", "0.0246", "532", "0.2242", "533",
			"0.2326", "534", "0.0131", "535", "0.0393", "536", "0.1472", "537", "0.0460", "538",
			"0.3250", "539", "0.0786", "540", "0.1282", "541", "0.2312", "542", "0.0299", "543",
			"0.0018", "544", "0.6300", "545", "0.1521", "546", "0.1237", "547", "0.1860", "548",
			"0.8333", "549", "0.2193", "550", "0.0902" };

	private static String[] x25_rawAP = new String[] { "501", "0.4110", "502", "0.0773", "503",
			"0.1626", "504", "0.4716", "505", "0.3757", "506", "0.1027", "507", "0.3420", "508",
			"0.2466", "509", "0.3990", "510", "0.6004", "511", "0.4031", "512", "0.1851", "513",
			"0.0955", "514", "0.1982", "515", "0.2083", "516", "0.0945", "517", "0.0364", "518",
			"0.2888", "519", "0.1244", "520", "0.1156", "521", "0.0284", "522", "0.3055", "523",
			"0.4668", "524", "0.0896", "525", "0.1190", "526", "0.0963", "527", "0.3581", "528",
			"0.4802", "529", "0.4014", "530", "0.6773", "531", "0.0246", "532", "0.2242", "533",
			"0.2267", "534", "0.0098", "535", "0.0325", "536", "0.1400", "537", "0.0542", "538",
			"0.3250", "539", "0.0922", "540", "0.1249", "541", "0.2271", "542", "0.0299", "543",
			"0.0018", "544", "0.6300", "545", "0.2363", "546", "0.1237", "547", "0.1860", "548",
			"0.5000", "549", "0.2528", "550", "0.0902" };

	private static String[] x30_rawAP = new String[] { "501", "0.4110", "502", "0.0949", "503",
			"0.1560", "504", "0.4974", "505", "0.3757", "506", "0.1027", "507", "0.3093", "508",
			"0.2468", "509", "0.4060", "510", "0.6705", "511", "0.4031", "512", "0.1986", "513",
			"0.0955", "514", "0.1747", "515", "0.2083", "516", "0.0945", "517", "0.0364", "518",
			"0.2888", "519", "0.1258", "520", "0.1167", "521", "0.0416", "522", "0.3095", "523",
			"0.4668", "524", "0.0853", "525", "0.1511", "526", "0.0963", "527", "0.4585", "528",
			"0.4802", "529", "0.3870", "530", "0.6777", "531", "0.0198", "532", "0.2242", "533",
			"0.2013", "534", "0.0083", "535", "0.0325", "536", "0.1436", "537", "0.0542", "538",
			"0.3250", "539", "0.0567", "540", "0.1249", "541", "0.2271", "542", "0.0309", "543",
			"0.0023", "544", "0.6301", "545", "0.3080", "546", "0.1336", "547", "0.1851", "548",
			"0.4500", "549", "0.2528", "550", "0.0886" };

	private static String[] x35_rawAP = new String[] { "501", "0.4017", "502", "0.0949", "503",
			"0.1560", "504", "0.4974", "505", "0.3889", "506", "0.1027", "507", "0.3093", "508",
			"0.2412", "509", "0.4060", "510", "0.6705", "511", "0.4086", "512", "0.1986", "513",
			"0.0955", "514", "0.1747", "515", "0.2129", "516", "0.0945", "517", "0.0342", "518",
			"0.2852", "519", "0.1258", "520", "0.1167", "521", "0.0389", "522", "0.2717", "523",
			"0.4668", "524", "0.0853", "525", "0.1511", "526", "0.0963", "527", "0.4585", "528",
			"0.4800", "529", "0.3870", "530", "0.6777", "531", "0.0198", "532", "0.2242", "533",
			"0.1739", "534", "0.0083", "535", "0.0329", "536", "0.1557", "537", "0.0564", "538",
			"0.3250", "539", "0.0510", "540", "0.1214", "541", "0.2320", "542", "0.0309", "543",
			"0.0023", "544", "0.6301", "545", "0.2968", "546", "0.1336", "547", "0.1851", "548",
			"0.4500", "549", "0.2674", "550", "0.0886" };

	private static String[] x40_rawAP = new String[] { "501", "0.3962", "502", "0.1169", "503",
			"0.1509", "504", "0.4882", "505", "0.3803", "506", "0.1028", "507", "0.3096", "508",
			"0.2435", "509", "0.3986", "510", "0.7028", "511", "0.3967", "512", "0.2019", "513",
			"0.0955", "514", "0.1606", "515", "0.2204", "516", "0.0945", "517", "0.0339", "518",
			"0.2852", "519", "0.1255", "520", "0.1192", "521", "0.0402", "522", "0.3944", "523",
			"0.4668", "524", "0.0834", "525", "0.1551", "526", "0.0963", "527", "0.4962", "528",
			"0.4818", "529", "0.3533", "530", "0.6763", "531", "0.0985", "532", "0.2242", "533",
			"0.1622", "534", "0.0082", "535", "0.0530", "536", "0.1592", "537", "0.0496", "538",
			"0.3250", "539", "0.0473", "540", "0.1210", "541", "0.2650", "542", "0.0173", "543",
			"0.0021", "544", "0.6231", "545", "0.2943", "546", "0.1306", "547", "0.1793", "548",
			"0.4500", "549", "0.2669", "550", "0.0875" };

	private static String[] x45_rawAP = new String[] { "501", "0.3871", "502", "0.1169", "503",
			"0.1509", "504", "0.4882", "505", "0.3984", "506", "0.1028", "507", "0.3096", "508",
			"0.2656", "509", "0.3986", "510", "0.7028", "511", "0.3948", "512", "0.2019", "513",
			"0.0955", "514", "0.1606", "515", "0.2234", "516", "0.0945", "517", "0.0312", "518",
			"0.2731", "519", "0.1255", "520", "0.1192", "521", "0.0506", "522", "0.4771", "523",
			"0.4668", "524", "0.0834", "525", "0.1551", "526", "0.0963", "527", "0.4962", "528",
			"0.5233", "529", "0.3533", "530", "0.6738", "531", "0.0985", "532", "0.2242", "533",
			"0.2004", "534", "0.0095", "535", "0.0684", "536", "0.1774", "537", "0.0459", "538",
			"0.3250", "539", "0.0475", "540", "0.1206", "541", "0.2773", "542", "0.0173", "543",
			"0.0021", "544", "0.6231", "545", "0.3027", "546", "0.1306", "547", "0.1793", "548",
			"0.4500", "549", "0.2612", "550", "0.0875" };

	private static String[] x50_rawAP = new String[] { "501", "0.3871", "502", "0.1169", "503",
			"0.1509", "504", "0.4882", "505", "0.3984", "506", "0.1028", "507", "0.3096", "508",
			"0.2903", "509", "0.3986", "510", "0.7028", "511", "0.3948", "512", "0.2019", "513",
			"0.0955", "514", "0.1606", "515", "0.2234", "516", "0.0945", "517", "0.0312", "518",
			"0.2731", "519", "0.1255", "520", "0.1192", "521", "0.0596", "522", "0.4815", "523",
			"0.4668", "524", "0.0834", "525", "0.1551", "526", "0.0963", "527", "0.4962", "528",
			"0.5233", "529", "0.3533", "530", "0.6738", "531", "0.0985", "532", "0.2242", "533",
			"0.2004", "534", "0.0097", "535", "0.0684", "536", "0.1782", "537", "0.0459", "538",
			"0.3250", "539", "0.0472", "540", "0.1206", "541", "0.2773", "542", "0.0173", "543",
			"0.0021", "544", "0.6231", "545", "0.3095", "546", "0.1306", "547", "0.1793", "548",
			"0.4500", "549", "0.2612", "550", "0.0875" };

	@Test
	public void runRegression() throws Exception {
		Map<String, GroundTruth> g = new HashMap<String, GroundTruth>();

		g.put("indep-x1.0", new GroundTruth("indep-x1.0", Metric.AP, 50, x10_rawAP, 0.1695f));
		g.put("indep-x1.5", new GroundTruth("indep-x1.5", Metric.AP, 50, x15_rawAP, 0.2143f));
		g.put("indep-x2.0", new GroundTruth("indep-x2.0", Metric.AP, 50, x20_rawAP, 0.2292f));
		g.put("indep-x2.5", new GroundTruth("indep-x2.5", Metric.AP, 50, x25_rawAP, 0.2299f));
		g.put("indep-x3.0", new GroundTruth("indep-x3.0", Metric.AP, 50, x30_rawAP, 0.2333f));
		g.put("indep-x3.5", new GroundTruth("indep-x3.5", Metric.AP, 50, x35_rawAP, 0.2323f));
		g.put("indep-x4.0", new GroundTruth("indep-x4.0", Metric.AP, 50, x40_rawAP, 0.2366f));
		g.put("indep-x4.5", new GroundTruth("indep-x4.5", Metric.AP, 50, x45_rawAP, 0.2413f));
		g.put("indep-x5.0", new GroundTruth("indep-x5.0", Metric.AP, 50, x50_rawAP, 0.2422f));

		Qrels qrels = new Qrels("docs/data/wt10g/qrels.wt10g");

		String[] params = new String[] { "docs/data/wt10g/run.wt10g.CIKM2010.title.indep.xml",
				"docs/data/wt10g/wt10g_queries_501-550.xml" };

		FileSystem fs = FileSystem.getLocal(new Configuration());

		BatchQueryRunner qr = new BatchQueryRunner(params, fs);

		long start = System.currentTimeMillis();
		qr.runQueries();
		long end = System.currentTimeMillis();

		sLogger.info("Total query time: " + (end - start) + "ms");

		DocnoMapping mapping = qr.getDocnoMapping();

		for (String model : qr.getModels()) {
			sLogger.info("Verifying results of model \"" + model + "\"");

			Map<String, Accumulator[]> results = qr.getResults(model);
			g.get(model).verify(results, mapping, qrels);

			sLogger.info("Done!");
		}
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(Wt10g_CIKM2010_Title_Indep.class);
	}
}