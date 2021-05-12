package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class JMeterResults {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Result> results;
	
	private String systemDate = "";
	
	private String name = "";

	public JMeterResults() {
		this.results = new ArrayList<Result>();
		systemDate = new SimpleDateFormat("yyyy/MM//dd").format(new Date(System.currentTimeMillis()));
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(ArrayList<Result> results) {
		this.results = results;
	}

	public void addResult(Result r) {
		this.results.add(r);
	}

	public void loadFile(File file) {
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			line = br.readLine();
			while((line = br.readLine()) != null){
				String [] fields = line.split(",");
				Result r = new Result(fields[0],fields[1],fields[2],fields[3],fields[4],fields[5],fields[6],fields[7],
						fields[8],fields[9],fields[10],fields[11],fields[12],fields[13],fields[14],fields[15]);
				addResult(r);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSystemDate() {
		return systemDate;
	}

	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
