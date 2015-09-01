package microlocation.models;

public class Beacon 
{
	private String uuid;
	private String major;
	private String minor;
	private String name;
	private String usecase;
	
	public Beacon(String uuid, String major, String minor, String name, String usecase)
	{
		this.uuid = uuid;
		this.major = major;
		this.minor = minor;
		this.name = name;
		this.usecase = usecase;
	}	
	
	public String getUUID ()
	{
		return uuid;
	}
	
	public String getMajor ()
	{
		return major;
	}
	
	public String getMinor ()
	{
		return minor;
	}
	
	public String getName ()
	{
		return name;
	}
	
	public String getUseCase ()
	{
		return usecase;
	}
}
