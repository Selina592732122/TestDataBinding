package test.com.testdatabinding;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpActivity extends AppCompatActivity {
	
	private static final String TAG = "Contacts";
	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

	//	private void insertDummyContactWrapper() {
	//		int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
	//		if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
	//			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS},
	//					REQUEST_CODE_ASK_PERMISSIONS);
	//			return;
	//		}
	//		insertDummyContact();
	//	}

	//	private void insertDummyContactWrapper() {
	//		int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS);
	//		if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
	//			if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_CONTACTS)) {
	//				//当一个权限第一次被请求和用户标记过不再提醒的时候,我们写的对话框被展示。
	//				showMessageOKCancel("You need to allow access to Contacts",
	//						new DialogInterface.OnClickListener() {
	//							@Override
	//							public void onClick(DialogInterface dialog, int which) {
	//								ActivityCompat.requestPermissions(HttpActivity.this,new String[] {Manifest.permission.WRITE_CONTACTS},
	//										REQUEST_CODE_ASK_PERMISSIONS);
	//							}
	//						});
	//				return;
	//			}
	//			ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_CONTACTS},
	//					REQUEST_CODE_ASK_PERMISSIONS);
	//			return;
	//		}
	//		insertDummyContact();
	//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http);
		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				insertDummyContactWrapper();
			}
		});
	}

	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(this)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	//多个权限
	private void insertDummyContactWrapper() {
		List<String> permissionsNeeded = new ArrayList<String>();

		final List<String> permissionsList = new ArrayList<String>();
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
			permissionsNeeded.add("GPS");
		if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
			permissionsNeeded.add("Read Contacts");
		if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
			permissionsNeeded.add("Write Contacts");

		if (permissionsList.size() > 0) {
			if (permissionsNeeded.size() > 0) {
				// Need Rationale
				String message = "You need to grant access to " + permissionsNeeded.get(0);
				for (int i = 1; i < permissionsNeeded.size(); i++)
					message = message + ", " + permissionsNeeded.get(i);
				showMessageOKCancel(message,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ActivityCompat.requestPermissions(HttpActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
										REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
							}
						});
				return;
			}
			ActivityCompat.requestPermissions(HttpActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
					REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
			return;
		}

		insertDummyContact();
	}

	private boolean addPermission(List<String> permissionsList, String permission) {
		if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(permission);
			// Check for Rationale Option
			if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
				return false;
		}
		return true;
	}


	private void insertDummyContact() {
		// Two operations are needed to insert a new contact. 
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);
		
		// First, set up a new raw contact. 
		ContentProviderOperation.Builder op =
				ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
						.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
						.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
		operations.add(op.build());
		
		// Next, set the name for the contact. 
		op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
						"__DUMMY CONTACT from runtime permissions sample");
		operations.add(op.build());
		
		// Apply the operations. 
		ContentResolver resolver = getContentResolver();
		try {
			resolver.applyBatch(ContactsContract.AUTHORITY, operations);
		} catch (RemoteException e) {
			Log.d(TAG, "Could not add a new contact: " + e.getMessage());
		} catch (OperationApplicationException e) {
			Log.d(TAG, "Could not add a new contact: " + e.getMessage());
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					insertDummyContact();
				} else {
					// Permission Denied
					Toast.makeText(this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
							.show();
				}
				break;

			case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
				Map<String, Integer> perms = new HashMap<String, Integer>();
				// Initial
				perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);
				// Fill with results
				for (int i = 0; i < permissions.length; i++)
					perms.put(permissions[i], grantResults[i]);
				// Check for ACCESS_FINE_LOCATION
				if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
					// All Permissions Granted
					insertDummyContact();
				} else {
					// Permission Denied
					Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;

			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	
}
