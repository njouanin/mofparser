package org.escapek.mofparser.samples;

import org.escapek.mofparser.DataType;
import org.escapek.mofparser.MOFParser;
import org.escapek.mofparser.decl.InstDecl;
import org.escapek.mofparser.decl.InstancePropertyDecl;
import org.escapek.mofparser.decl.TypeDecl;

public class Sample2 {
	public static void main(String[] args) {

		//Create instance declaration
		InstDecl instance = new InstDecl();
		instance.className = "Acme_LogicalDisk";
		instance.alias = "Disk";

		//Create properties
		InstancePropertyDecl driveLetter = new InstancePropertyDecl();
		driveLetter.name = "DriveLetter";
		driveLetter.type = new TypeDecl(DataType.STRING.toString());
		driveLetter.value.add("C");
		
		InstancePropertyDecl volumeLabel = new InstancePropertyDecl();
		volumeLabel.name = "VolumeLabel";
		volumeLabel.type = new TypeDecl(DataType.STRING.toString());
		volumeLabel.value.add("myvol");

		InstancePropertyDecl dependent = new InstancePropertyDecl();
		dependent.name = "Dependent";
		dependent.type = new TypeDecl(DataType.STRING.toString());
		dependent.value.add("CIM_Service.Name = \\\"mail\\\"");

		InstancePropertyDecl ipAddresses = new InstancePropertyDecl();
		ipAddresses.name = "ip_addresses";
		ipAddresses.type = new TypeDecl(DataType.STRING.toString());
		ipAddresses.type.isArray = true;
		ipAddresses.value.add("1.2.3.4");
		ipAddresses.value.add("1.2.3.5");
		ipAddresses.value.add("1.2.3.7");

		//Add properties to instance
		instance.properties.add(driveLetter);
		instance.properties.add(volumeLabel);
		instance.properties.add(dependent);
		instance.properties.add(ipAddresses);

		//Create parser instance
		MOFParser parser = new MOFParser();
		//Generate content
		String mofContent = parser.generateMOF(instance);
		System.out.println(mofContent);
	}
}
