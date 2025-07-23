// ファイルの中身をこれで完全に置き換えてください
package com.hotel;

import java.util.Scanner;
import java.util.Date;

public class HotelReservationSystem {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    ReservationControl reservationControl = new ReservationControl();
    CheckInControl checkInControl = new CheckInControl();
    CheckOutControl checkOutControl = new CheckOutControl();

    while (true) {
      System.out.println("\n1. 予約\n2. チェックイン\n3. チェックアウト\n4. 予約キャンセル\n5. 終了");
      System.out.print("> ");
      String input = scanner.nextLine();

      if (input.equals("1")) {
        System.out.println("チェックイン日 (YYYY/MM/DD):");
        String checkinStr = scanner.nextLine();
        Date checkinDate = DateUtil.convertToDate(checkinStr);

        System.out.println("チェックアウト日 (YYYY/MM/DD):");
        String checkoutStr = scanner.nextLine();
        Date checkoutDate = DateUtil.convertToDate(checkoutStr);

        System.out.println("部屋タイプ (Single/Double/Suite):");
        String roomType = scanner.nextLine();

        if (checkinDate == null || checkoutDate == null || roomType.isEmpty()) {
            System.out.println("入力が正しくありません。");
            continue;
        }

        String resId = reservationControl.reserveRoom(checkinDate, checkoutDate, roomType);
        if (resId != null) {
          System.out.println("予約番号: " + resId);
        }

      } else if (input.equals("2")) {
        System.out.println("予約番号を入力:");
        String resId = scanner.nextLine();
        checkInControl.checkIn(resId);

      } else if (input.equals("3")) {
        System.out.println("部屋番号を入力:");
        String roomNo = scanner.nextLine();
        checkOutControl.checkOut(roomNo);

      } else if (input.equals("4")) {
        System.out.println("予約番号を入力:");
        String resId = scanner.nextLine();
        reservationControl.cancelReservation(resId);

      } else if (input.equals("5")) {
        System.out.println("終了します。");
        scanner.close();
        break;
      } else {
        System.out.println("無効な選択です。");
      }
    }
  }
}